#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <map>
#include <set>
#include <string>
#include <algorithm>

#define SPLIT_STRING(str, delim) [&](const std::string& s) { \
    std::vector<std::string> tokens; \
    std::string token; \
    std::istringstream tokenStream(s); \
    while (std::getline(tokenStream, token, delim)) { \
        tokens.push_back(token); \
    } \
    return tokens; \
}(str)

using namespace std;

// Set of special packages that will be clustered separately
const set<string> special_packages = {
    "java", "Math", "com.sun", "javax", "jdk.internal", "META-INF.services", "sun"
};

// Struct to hold the imports for each class
struct ImportInfo {
    string className;
    vector<string> imports;
};

// Function to read the output.txt file and extract import information
vector<ImportInfo> read_imports(const string& filename) {
    vector<ImportInfo> import_data;
    ifstream file(filename);
    string line, current_class;
    
    while (getline(file, line)) {
        if (line.find("imports:") != string::npos) {
            current_class = line.substr(0, line.find(" imports:"));
            import_data.push_back(ImportInfo{current_class, {}});
        } else if (!line.empty() && line[0] == '\t') {
            import_data.back().imports.push_back(line.substr(1));
        }
    }
    return import_data;
}

// Function to create a co-occurrence matrix from the import information
void create_cooccurrence_matrix(const vector<ImportInfo>& imports, vector<vector<int>>& matrix, vector<string>& import_names) {
    map<string, int> import_indices;
    int index = 0;

    // Collect all unique imports
    for (const auto& info : imports) {
        for (const auto& imp : info.imports) {
            if (import_indices.find(imp) == import_indices.end()) {
                import_indices[imp] = index++;
                import_names.push_back(imp);
            }
        }
    }

    // Initialize the co-occurrence matrix
    matrix.resize(import_names.size(), vector<int>(import_names.size(), 0));

    // Fill the co-occurrence matrix based on the imports in each class
    for (const auto& info : imports) {
        const auto& current_imports = info.imports;
        for (size_t i = 0; i < current_imports.size(); ++i) {
            for (size_t j = i + 1; j < current_imports.size(); ++j) {
                int index_i = import_indices[current_imports[i]];
                int index_j = import_indices[current_imports[j]];
                matrix[index_i][index_j]++;
                matrix[index_j][index_i]++;
            }
        }
    }
}

// Function to perform clustering based on the co-occurrence matrix
map<int, set<string>> perform_clustering(const vector<vector<int>>& matrix, const vector<string>& import_names) {
    map<int, set<string>> clusters;
    vector<bool> clustered(import_names.size(), false);
    int current_cluster = 0;

    // Special packages are assigned their own clusters
    for (size_t i = 0; i < import_names.size(); ++i) {
        auto parts = SPLIT_STRING(import_names[i], '.');
        if (special_packages.find(parts[0]) != special_packages.end()) {
            if (clusters.find(current_cluster) == clusters.end()) {
                clusters[current_cluster] = set<string>();
            }
            clusters[current_cluster].insert(import_names[i]);
            clustered[i] = true;
        }
    }

    current_cluster++; // Move to the next cluster for general imports

    // Create clusters based on co-occurrence for the rest of the imports
    for (size_t i = 0; i < import_names.size(); ++i) {
        if (!clustered[i]) {
            set<string> cluster;
            cluster.insert(import_names[i]);
            clustered[i] = true;

            // Add all related imports/classes to the same cluster
            for (size_t j = 0; j < import_names.size(); ++j) {
                if (i != j && matrix[i][j] > 0 && !clustered[j]) {
                    cluster.insert(import_names[j]);
                    clustered[j] = true;
                }
            }

            clusters[current_cluster++] = cluster;
        }
    }

    return clusters;
}

// Function to write the RSF file output for clusters
void write_rsf_file(const map<int, set<string>>& clusters, const string& filename) {
    ofstream rsf_file(filename);
    if (!rsf_file.is_open()) {
        cerr << "Error: Could not open RSF file: " << filename << endl;
        return;
    }

    for (const auto& cluster : clusters) {
        for (const auto& item : cluster.second) {
            rsf_file << "contain " << cluster.first << "\t" << item << endl;
        }
    }

    rsf_file.close();
    if (!rsf_file) {
        cerr << "Error: Could not successfully close file: " << filename << endl;
    }
}

// Main function
int main() {
    // Input and output file paths
    string input_txt = "C:\\Users\\kalma\\OneDrive\\Belgeler\\GitHub\\CS401\\src\\Test\\output.txt";
    string output_rsf = "C:\\Users\\kalma\\OneDrive\\Belgeler\\GitHub\\CS401\\src\\Clusterer\\import_clusters.rsf";

    // Step 1: Read imports from the output.txt file
    auto imports = read_imports(input_txt);

    // Step 2: Create the co-occurrence matrix from the imports
    vector<vector<int>> cooccurrence_matrix;
    vector<string> import_names;
    create_cooccurrence_matrix(imports, cooccurrence_matrix, import_names);

    // Step 3: Perform clustering based on the co-occurrence matrix
    auto clusters = perform_clustering(cooccurrence_matrix, import_names);

    // Step 4: Write the RSF file with the correct format
    write_rsf_file(clusters, output_rsf);

    cout << "Clustering and RSF output written to 'import_clusters.rsf'." << endl;

    return 0;
}
