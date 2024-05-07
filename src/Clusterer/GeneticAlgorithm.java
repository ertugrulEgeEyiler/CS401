
package Clusterer;

import java.io.*;
import java.util.*;

public class GeneticAlgorithm extends Clustering {


    private int populationSize;
    private int numClusters;
    private int maxGenerations;
    private Random random;
    private double mutationRate;

    public GeneticAlgorithm(int populationSize, int numClusters, int maxGenerations, double mutationRate) {
        this.populationSize = populationSize;
        this.numClusters = numClusters;
        this.maxGenerations = maxGenerations;
        this.mutationRate = mutationRate;
        this.random = new Random();
    }

    @Override
    public Map<String, Integer> readImports(String inputFile) throws IOException {
        Map<String, Integer> imports = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.trim().endsWith("imports:")) {
                    imports.put(line.trim(), random.nextInt(numClusters));
                }
            }
        }
        return imports;
    }

    @Override
    public void findClusters(String inputFile, String outputFile) throws IOException {
        List<Map<String, Integer>> population = initializePopulation(inputFile);
        Map<String, Integer> bestSolution = null;
        double bestFitness = Double.MAX_VALUE;

        for (int generation = 0; generation < maxGenerations; generation++) {
            List<Map<String, Integer>> newPopulation = new ArrayList<>();
            for (int i = 0; i < populationSize; i++) {
                Map<String, Integer> parent1 = select(population);
                Map<String, Integer> parent2 = select(population);
                Map<String, Integer> child = crossover(parent1, parent2);
                mutate(child);
                newPopulation.add(child);
                double childFitness = calculateFitness(child);
                if (childFitness < bestFitness) {
                    bestFitness = childFitness;
                    bestSolution = child;
                }
            }
            population = newPopulation;
        }
        writeClusters(convertToOutputFormat(bestSolution), outputFile);
    }

    private List<Map<String, Integer>> initializePopulation(String inputFile) throws IOException {
        List<Map<String, Integer>> population = new ArrayList<>();
        Map<String, Integer> initialImports = readImports(inputFile);
        for (int i = 0; i < populationSize; i++) {
            Map<String, Integer> individual = new HashMap<>(initialImports);
            for (String key : individual.keySet()) {
                individual.put(key, random.nextInt(numClusters));
            }
            population.add(individual);
        }
        return population;
    }

    private Map<String, Integer> select(List<Map<String, Integer>> population) {
        // Simple roulette wheel selection based on fitness
        double totalFitness = population.stream().mapToDouble(this::calculateFitness).sum();
        double slice = random.nextDouble() * totalFitness;
        double current = 0;
        for (Map<String, Integer> individual : population) {
            current += calculateFitness(individual);
            if (current >= slice) {
                return individual;
            }
        }
        return population.get(random.nextInt(populationSize));
    }

    private Map<String, Integer> crossover(Map<String, Integer> parent1, Map<String, Integer> parent2) {
        Map<String, Integer> child = new HashMap<>();
        List<String> keys = new ArrayList<>(parent1.keySet());
        int splitPoint = random.nextInt(keys.size());
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            child.put(key, i < splitPoint ? parent1.get(key) : parent2.get(key));
        }
        return child;
    }

    private void mutate(Map<String, Integer> individual) {
        for (String key : individual.keySet()) {
            if (random.nextDouble() < mutationRate) {
                individual.put(key, random.nextInt(numClusters));
            }
        }
    }

    private double calculateFitness(Map<String, Integer> individual) {
        // This is a placeholder for a real fitness function
        // Typically, this would involve calculating intra-cluster and inter-cluster distances
        return individual.values().stream().distinct().count();
    }

    private Map<Integer, StringBuilder> convertToOutputFormat(Map<String, Integer> clusterData) {
        Map<Integer, StringBuilder> outputFormat = new TreeMap<>();
        for (Map.Entry<String, Integer> entry : clusterData.entrySet()) {
            int clusterId = entry.getValue();
            String importItem = entry.getKey();
            outputFormat.computeIfAbsent(clusterId, k -> new StringBuilder()).append(importItem).append("\n");
        }
        return outputFormat;
    }

    public void writeClusters(Map<Integer, StringBuilder> clusters, String outputFile) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            for (Map.Entry<Integer, StringBuilder> entry : clusters.entrySet()) {
                writer.write("Cluster " + entry.getKey() + ":\n");
                writer.write(entry.getValue().toString());
            }
        }
    }
}

