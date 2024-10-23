#include <iostream>
#include <fstream>
#include <vector>
#include <map>
#include <set>
#include <sstream>
#include <algorithm>

using namespace std;

// Helper function to split strings by a delimiter
vector<string> split(const string &str, char delimiter) {
    vector<string> tokens;
    string token;
    istringstream tokenStream(str);
    while (getline(tokenStream, token, delimiter)) {
        tokens.push_back(token);
    }
    return tokens;
}

// Function to parse the import data from the output.txt
map<string, set<string>> parseImports(const string &filePath) {
    ifstream file(filePath);
    map<string, set<string>> fileImports;
    string line, currentFile;
    
    while (getline(file, line)) {
        if (line.find("imports:") != string::npos) {
            currentFile = line.substr(0, line.find(" imports"));
            cout << "Processing file: " << currentFile << endl;  // Debugging: Which file is being processed
        } else {
            fileImports[currentFile].insert(line);
            cout << "Found import: " << line << endl;  // Debugging: Which imports are found
        }
    }
    
    file.close();
    return fileImports;
}

// Function to cluster java.<package> imports separately and return a list of java clusters
map<string, set<string>> clusterJavaPackages(const map<string, set<string>>& fileImports, vector<string>& javaImportsList) {
    map<string, set<string>> javaClusters;

    for (const auto& [file, imports] : fileImports) {
        for (const string& imp : imports) {
            if (imp.find("java.") == 0) {  // If the import starts with "java."
                vector<string> parts = split(imp, '.');
                if (parts.size() > 1) {
                    string packageName = "java." + parts[1];  // Take "java.io", "java.util", etc.
                    javaClusters[packageName].insert(imp);  // Add the full import to the corresponding cluster
                    javaImportsList.push_back(imp); // Add to the java imports list

                    // Debugging: Show which java import is added to the list
                    cout << "Added java import to list: " << imp << " from file: " << file << endl;
                }
            }
        }
    }

    return javaClusters;
}

// Function to generate co-occurrence matrix for non-java imports
map<pair<string, string>, int> generateCoOccurrenceMatrix(const map<string, set<string>>& fileImports) {
    map<pair<string, string>, int> coOccurrenceMatrix;

    for (const auto& [file, imports] : fileImports) {
        for (const string& imp1 : imports) {
            if (imp1.find("java.") == string::npos) {  // Exclude java imports
                for (const string& imp2 : imports) {
                    if (imp1 != imp2 && imp2.find("java.") == string::npos) {
                        pair<string, string> importPair = make_pair(min(imp1, imp2), max(imp1, imp2));
                        coOccurrenceMatrix[importPair]++;
                    }
                }
            }
        }
    }

    return coOccurrenceMatrix;
}

// Function to perform clustering based on co-occurrences (for non-java imports)
vector<vector<string>> clusterImports(const map<pair<string, string>, int>& coOccurrenceMatrix, int threshold) {
    map<string, int> clusterMap;
    vector<vector<string>> clusters;
    
    for (const auto& [importPair, count] : coOccurrenceMatrix) {
        if (count >= threshold) {
            string imp1 = importPair.first;
            string imp2 = importPair.second;
            
            if (clusterMap.find(imp1) == clusterMap.end() && clusterMap.find(imp2) == clusterMap.end()) {
                // New cluster
                clusters.push_back({imp1, imp2});
                int clusterIndex = clusters.size() - 1;
                clusterMap[imp1] = clusterIndex;
                clusterMap[imp2] = clusterIndex;
            } else if (clusterMap.find(imp1) != clusterMap.end() && clusterMap.find(imp2) == clusterMap.end()) {
                // Add imp2 to imp1's cluster
                int clusterIndex = clusterMap[imp1];
                clusters[clusterIndex].push_back(imp2);
                clusterMap[imp2] = clusterIndex;
            } else if (clusterMap.find(imp1) == clusterMap.end() && clusterMap.find(imp2) != clusterMap.end()) {
                // Add imp1 to imp2's cluster
                int clusterIndex = clusterMap[imp2];
                clusters[clusterIndex].push_back(imp1);
                clusterMap[imp1] = clusterIndex;
            }
        }
    }
    
    return clusters;
}

// Function to write clusters and java.<package> imports to matrixAlgorithm.rsf file
void writeClustersToFile(const map<string, set<string>>& javaClusters, const vector<vector<string>>& nonJavaClusters, const string &outputFilePath, const vector<string>& javaImportsList) {
    ofstream outFile(outputFilePath);

    int clusterId = 0;

    // Write java clusters
    for (const auto& [packageName, imports] : javaClusters) {
        cout << "Writing java cluster: " << packageName << " with " << imports.size() << " imports" << endl;  // Debugging
        for (const string& imp : imports) {
            outFile << "contain " << clusterId << " " << imp << endl;
        }
        clusterId++;
    }

    // Write non-java clusters
    for (const auto& cluster : nonJavaClusters) {
        for (const string& imp : cluster) {
            outFile << "contain " << clusterId << " " << imp << endl;
        }
        clusterId++;
    }

    // Write the list of all java.<package> imports separately
    outFile << "List of all java imports:" << endl;
    for (const auto& imp : javaImportsList) {
        outFile << imp << endl;
    }

    outFile.close();
}

int main(int argc, char* argv[]) {
    if (argc != 3) {
        cerr << "Usage: " << argv[0] << " <input_file> <output_file>" << endl;
        return 1;
    }

    string inputFilePath = argv[1];  // First command-line argument is input file
    string outputFilePath = argv[2];  // Second command-line argument is output file
    
    // Parse imports from file
    map<string, set<string>> fileImports = parseImports(inputFilePath);
    
    // Prepare a list to store all java.<package> imports
    vector<string> javaImportsList;
    
    // Cluster java packages separately and populate javaImportsList
    map<string, set<string>> javaClusters = clusterJavaPackages(fileImports, javaImportsList);
    
    // Generate co-occurrence matrix for non-java imports
    map<pair<string, string>, int> coOccurrenceMatrix = generateCoOccurrenceMatrix(fileImports);
    
    // Perform clustering for non-java imports
    int threshold = 2;  // Define your threshold for clustering
    vector<vector<string>> nonJavaClusters = clusterImports(coOccurrenceMatrix, threshold);
    
    // Write the clusters and java imports to matrixAlgorithm.rsf file
    writeClustersToFile(javaClusters, nonJavaClusters, outputFilePath, javaImportsList);
    
    cout << "Clustering completed and saved to " << outputFilePath << endl;
    
    return 0;
}
