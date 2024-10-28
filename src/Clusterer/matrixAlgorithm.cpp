#include <iostream>
#include <fstream>
#include <map>
#include <set>
#include <vector>
#include <string>
#include <sstream>
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
        } else if (!line.empty()) {
            import_data.back().imports.push_back(line);
        }
    }
    return import_data;
}

// Function to extract cluster key based on special packages or general imports
string extract_cluster_key(const string& import_statement) {
    auto parts = SPLIT_STRING(import_statement, '.');

    // Check if the package starts with one of the special packages
    if (special_packages.find(parts[0]) != special_packages.end()) {
        if (parts.size() > 1) {
            return parts[0] + "." + parts[1];  // e.g. java.util, java.io
        } else {
            return parts[0];  // e.g. just java or javax
        }
    }

    // Return a general key for non-special packages
    if (parts.size() > 1) {
        return parts[0] + "." + parts[1];  // for other imports
    }
    return import_statement;
}

// Function to filter and process imports for clustering
map<string, set<string>> cluster_imports(const vector<ImportInfo>& imports) {
    map<string, set<string>> clusters;
    
    for (const auto& info : imports) {
        for (const auto& imp : info.imports) {
            string cluster_key = extract_cluster_key(imp);
            if (!cluster_key.empty()) {
                clusters[cluster_key].insert(info.className);  // Cluster imports by package/class name
            }
        }
    }
    return clusters;
}

// Function to create a mapping of cluster keys to numbers
map<string, int> create_class_mapping(const map<string, set<string>>& clusters) {
    map<string, int> class_mapping;
    int current_number = 0;

    // Number the clusters sequentially
    for (const auto& cluster : clusters) {
        if (class_mapping.find(cluster.first) == class_mapping.end()) {
            class_mapping[cluster.first] = current_number++;
        }
    }

    return class_mapping;
}

// Function to write the RSF file output for clusters
void write_rsf_file(const map<string, set<string>>& clusters, const map<string, int>& class_mapping, const string& filename) {
    ofstream rsf_file(filename);
    if (!rsf_file.is_open()) {
        cerr << "Error: Could not open RSF file: " << filename << endl;
        return;
    }

    for (const auto& cluster : clusters) {
        int cluster_number = class_mapping.at(cluster.first);
        for (const auto& import_name : cluster.second) {
            rsf_file << "contain " << cluster_number << " " << cluster.first << " " << import_name << endl;
        }
    }

    rsf_file.close();
    if (!rsf_file) {
        cerr << "Error: Could not successfully close file: " << filename << endl;
    }
}

// Function to create co-occurrence matrix
vector<vector<int>> create_cooccurrence_matrix(const map<string, set<string>>& clusters, vector<string>& unique_imports) {
    map<string, int> import_indices;
    int index = 0;

    // Get all unique import names
    for (const auto& cluster : clusters) {
        for (const auto& import_name : cluster.second) {
            if (find(unique_imports.begin(), unique_imports.end(), import_name) == unique_imports.end()) {
                unique_imports.push_back(import_name);
                import_indices[import_name] = index++;
            }
        }
    }

    // Initialize the co-occurrence matrix
    vector<vector<int>> matrix(unique_imports.size(), vector<int>(unique_imports.size(), 0));

    // Fill the co-occurrence matrix
    for (const auto& cluster : clusters) {
        const auto& import_set = cluster.second;
        for (auto it1 = import_set.begin(); it1 != import_set.end(); ++it1) {
            for (auto it2 = next(it1); it2 != import_set.end(); ++it2) {
                matrix[import_indices[*it1]][import_indices[*it2]]++;
                matrix[import_indices[*it2]][import_indices[*it1]]++;
            }
        }
    }

    return matrix;
}

// Function to write the co-occurrence matrix to a CSV file
void write_matrix_to_csv(const vector<vector<int>>& matrix, const vector<string>& import_names, const string& filename) {
    ofstream csv_file(filename);
    if (!csv_file.is_open()) {
        cerr << "Error: Could not open CSV file: " << filename << endl;
        return;
    }

    // Write the header row
    csv_file << "Import Name,";
    for (const auto& import_name : import_names) {
        csv_file << import_name << ",";
    }
    csv_file << endl;

    // Write the co-occurrence matrix
    for (size_t i = 0; i < matrix.size(); ++i) {
        csv_file << import_names[i] << ",";
        for (size_t j = 0; j < matrix[i].size(); ++j) {
            csv_file << matrix[i][j] << ",";
        }
        csv_file << endl;
    }

    csv_file.close();
}

// Main function
int main() {
    // Step 1: Read imports from the file
    auto imports = read_imports("C:\\Users\\kalma\\OneDrive\\Belgeler\\GitHub\\CS401\\src\\Test\\output.txt");

    // Step 2: Process imports and cluster them together
    auto clusters = cluster_imports(imports);

    // Step 3: Create a mapping for clusters
    auto class_mapping = create_class_mapping(clusters);

    // Step 4: Write the RSF file with the correct format
    write_rsf_file(clusters, class_mapping, "C:\\Users\\kalma\\OneDrive\\Belgeler\\GitHub\\CS401\\src\\Clusterer\\import_clusters.rsf");

    // Step 5: Create co-occurrence matrix
    vector<string> unique_imports;
    auto cooccurrence_matrix = create_cooccurrence_matrix(clusters, unique_imports);

    // Step 6: Write the co-occurrence matrix to CSV
    write_matrix_to_csv(cooccurrence_matrix, unique_imports, "C:\\Users\\kalma\\OneDrive\\Belgeler\\GitHub\\CS401\\src\\Clusterer\\co_occurrence_matrix.csv");

    cout << "Clustering and CSV output written to 'import_clusters.rsf' and 'co_occurrence_matrix.csv'." << endl;

    return 0;
}
