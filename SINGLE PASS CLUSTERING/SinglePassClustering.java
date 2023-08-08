import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SinglePassClustering {
    public static void main(String[] args) throws IOException {
        BufferedReader stdInpt = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter the number of Tokens:");
        int noOfTokens = Integer.parseInt(stdInpt.readLine());

        System.out.println("Enter the number of Documents:");
        int noOfDocuments = Integer.parseInt(stdInpt.readLine());

        System.out.println("Enter the threshold:");
        float threshold = Float.parseFloat(stdInpt.readLine());

        System.out.println("Enter the Document Token Matrix:");

        int[][] input = new int[noOfDocuments][noOfTokens];

        for (int i = 0; i < noOfDocuments; i++) {
            for (int j = 0; j < noOfTokens; j++) {
                System.out.println("Enter (" + i + "," + j + "):");
                input[i][j] = Integer.parseInt(stdInpt.readLine());
            }
        }
        
        //Display Matrix
        
        
        for(int i = 0; i < noOfDocuments; i++){
            System.out.print(" "+i+" ");
        }
        System.out.println(" ");
        for (int i = 0; i < noOfDocuments; i++) {
            for (int j = 0; j < noOfTokens; j++) {
                System.out.print(" "+input[i][j]+" ");
            }
            System.out.print("\n");
        }
        

        singlePassAlgorithm(noOfDocuments, noOfTokens, threshold, input);
    }

    private static void singlePassAlgorithm(int noOfDocuments, int noOfTokens, float threshold, int[][] input) {
        int[][] cluster = new int[noOfDocuments][noOfDocuments + 1];
        ArrayList<Float[]> clusterRepresentative = new ArrayList<>();

        cluster[0][0] = 1; // First cluster
        cluster[0][1] = 0; // First document in the cluster
        int noOfClusters = 1;

        Float[] temp = new Float[noOfTokens];
        temp = convertIntArrayToFloatArray(input[0]);
        clusterRepresentative.add(temp);

        for (int i = 1; i < noOfDocuments; i++) {
            float maxSimilarity = -1;
            int clusterId = -1;

            for (int j = 0; j < noOfClusters; j++) {
                float similarity = calculateSimilarity(convertIntArrayToFloatArray(input[i]), clusterRepresentative.get(j));

                if (similarity > threshold && similarity > maxSimilarity) {
                    maxSimilarity = similarity;
                    clusterId = j;
                }
            }

            if (maxSimilarity == -1) {
                cluster[noOfClusters][0] = 1;
                cluster[noOfClusters][1] = i;
                noOfClusters++;
                clusterRepresentative.add(convertIntArrayToFloatArray(input[i]));
            } else {
                cluster[clusterId][0] += 1;
                int index = cluster[clusterId][0];
                cluster[clusterId][index] = i;
                clusterRepresentative.set(clusterId, calculateClusterRepresentative(cluster[clusterId], input, noOfTokens));
            }
        }

        for (int i = 0; i < noOfClusters; i++) {
            System.out.print("\nCluster " + i + ":");
            for (int j = 1; j <= cluster[i][0]; j++) {
                System.out.print(" " + cluster[i][j]);
            }
        }
    }

    private static Float[] convertIntArrayToFloatArray(int[] input) {
        int size = input.length;
        Float[] answer = new Float[size];
        for (int i = 0; i < size; i++) {
            answer[i] = (float) input[i];
        }
        return answer;
    }

    private static float calculateSimilarity(Float[] a, Float[] b) {
        float similarity = 0;
        for (int i = 0; i < a.length; i++) {
            similarity += a[i] * b[i];
        }
        return similarity;
    }

    private static Float[] calculateClusterRepresentative(int[] cluster, int[][] input, int noOfTokens) {
        Float[] answer = new Float[noOfTokens];
        for (int i = 0; i < noOfTokens; i++) {
            answer[i] = 0f;
        }
        for (int i = 1; i <= cluster[0]; i++) {
            for (int j = 0; j < noOfTokens; j++) {
                answer[j] += input[cluster[i]][j];
            }
        }
        for (int i = 0; i < noOfTokens; i++) {
            answer[i] /= cluster[0];
        }
        return answer;
    }
}