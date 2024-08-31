/*
 * Name: TODO
 * PID:  TODO
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.*;

/**
 * Search Engine implementation.
 *
 * @author TODO
 * @since  TODO
 */
public class SearchEngine {

    /**
     * Populate BSTrees from a file
     *
     * @param movieTree  - BST to be populated with actors
     * @param studioTree - BST to be populated with studios
     * @param ratingTree - BST to be populated with ratings
     * @param fileName   - name of the input file
     * @returns false if file not found, true otherwise
     */
    public static boolean populateSearchTrees(
            BSTree<String> movieTree, BSTree<String> studioTree,
            BSTree<String> ratingTree, String fileName
    ) {
        // open and read file
        File file = new File(fileName);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                // read 5 lines per batch:
                // movie, cast, studios, rating, trailing hyphen
                String movie = scanner.nextLine().trim();
                String cast[] = scanner.nextLine().split(" ");
                String studios[] = scanner.nextLine().split(" ");
                String rating = scanner.nextLine().trim();
                scanner.nextLine();

                /* TODO */
                // populate three trees with the information you just read
                // hint: create a helper function and reuse it to build all three trees


                for (String actor : cast) {
                    String lowerCaseActor = actor.toLowerCase();
                    if (!movieTree.findKey(lowerCaseActor)) {
                        movieTree.insertData(lowerCaseActor, movie);
                    } else {
                        LinkedList<String> movies = movieTree.findDataList(lowerCaseActor);
                        if (!movies.contains(movie)) {
                            movies.add(movie);
                        }
                    }
                }

                // Populate studioTree
                for (String studio : studios) {
                    String lowerCaseStudio = studio.toLowerCase();
                    if (!studioTree.findKey(lowerCaseStudio)) {
                        studioTree.insertData(lowerCaseStudio, movie);
                    } else {
                        LinkedList<String> movies = studioTree.findDataList(lowerCaseStudio);
                        if (!movies.contains(movie)) {
                            movies.add(movie);
                        }
                    }
                }

                // Populate ratingTree
                for (String actor : cast) {
                    String lowerCaseActor = actor.toLowerCase();
                    if (!ratingTree.findKey(lowerCaseActor)) {
                        ratingTree.insertData(lowerCaseActor, rating);
                    } else {
                        LinkedList<String> ratings = ratingTree.findDataList(lowerCaseActor);
                        if (!ratings.contains(rating)) {
                            ratings.add(rating);
                        }
                    }
                }


            }
            scanner.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * Search a query in a BST
     *
     * @param searchTree - BST to be searched
     * @param query      - query string
     */
    public static void searchMyQuery(BSTree<String> searchTree, String query) {

        /* TODO */
        // process query
        String[] keys = query.toLowerCase().split(" ");

        // search and output intersection results
        // hint: list's addAll() and retainAll() methods could be helpful

        // search and output individual results
        // hint: list's addAll() and removeAll() methods could be helpful

         Set<String> commonKeys = new HashSet<>(Arrays.asList(keys));
    
        // Search for common keys
        LinkedList<String> commonResults = new LinkedList<>();
        for (String key : commonKeys) {
            LinkedList<String> results = searchTree.findDataList(key);
            if (!results.isEmpty()) {
                commonResults.addAll(results);
            }
        }
        
        // Search for individual keys
        Map<String, LinkedList<String>> individualResults = new HashMap<>();
        for (String key : keys) {
            LinkedList<String> results = searchTree.findDataList(key);
            if (!results.isEmpty()) {
                individualResults.put(key, results);
            }
        }
        
        // Combine results
        LinkedList<String> finalResults = new LinkedList<>();
        finalResults.addAll(commonResults);
        
        // Add unique results from individual searches
        for (Map.Entry<String, LinkedList<String>> entry : individualResults.entrySet()) {
            if (!commonResults.contains(entry.getValue())) {
                finalResults.addAll(entry.getValue());
            }
        }
        
        // Sort and print results
        Object[] sortedResults = finalResults.toArray();
        Arrays.sort(sortedResults);
        print(query, new LinkedList<>(Arrays.asList((String[])sortedResults)));



    }

    /**
     * Print output of query
     *
     * @param query     Query used to search tree
     * @param documents Output of documents from query
     */
    public static void print(String query, LinkedList<String> documents) {
        if (documents == null || documents.isEmpty())
            System.out.println("The search yielded no results for " + query);
        else {
            Object[] converted = documents.toArray();
            Arrays.sort(converted);
            System.out.println("Documents related to " + query
                    + " are: " + Arrays.toString(converted));
        }
    }

    /**
     * Main method that processes and query the given arguments
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

        // initialize search trees
        BSTree<String> movieTree = new BSTree<>();
        BSTree<String> studioTree = new BSTree<>();
        BSTree<String> ratingTree = new BSTree<>();

        // process command line arguments
        String fileName = args[0];
        int searchKind = Integer.parseInt(args[1]);

        // populate search trees
        if (!populateSearchTrees(movieTree, studioTree, ratingTree, fileName)) {
            System.out.println("File not found.");
            return;
        }

        // choose the right tree to query

        BSTree<String> searchTree = null;
        switch (searchKind) {
            case 0:
                searchTree = movieTree;
                break;
            case 1:
                searchTree = studioTree;
                break;
            case 2:
                searchTree = ratingTree;
                break;
            default:
                System.out.println("Invalid search kind.");
                return;
        }

        // Build the query from the remaining command line arguments
        StringBuilder query = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            query.append(args[i]).append(' ');
        }
        String queryString = query.toString().trim();

        // Call searchMyQuery
        searchMyQuery(searchTree, queryString);
    }
}