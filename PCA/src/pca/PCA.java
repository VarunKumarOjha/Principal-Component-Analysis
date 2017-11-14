/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pca;

import java.io.*;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 *
 * @author vojha
 */
public class PCA {

    //Global variables
    private double[][] mat; //matrix of n x m where n is numbe rof rows and m number of columns
    private double m[][];//holds cofactors of determinants
    private final String filePath = System.getProperty("user.dir") + File.separator + "data" + File.separator;
    private String data_file = "simple_mat.csv";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        PCA pca = new PCA();
        pca.start();

    }

    private void start() {

        //Read data and define matrices
        readData();
        print_matrix(mat,"Input X");

        //computing mean 
        double[] mu = mean(mat);// finding mu for training set features
        //Computing x-mean
        double[][] x_mean = x_minus_mean(mat, mu);
        print_matrix(x_mean,"X - mean");
        //computing varience
        double[] var = sigma2(x_mean, mu);// finding sigma2 for training set features
        //Computing covarience
        double[][] covar = sigmacovar(x_mean);
        print_matrix(covar,"Covarience");
        
        Matrix A = new Matrix(covar);
        SingularValueDecomposition s = A.svd();
        System.out.print("U = ");
        Matrix U = s.getU();
        
        System.out.println("All principle components");
        U.print(mat.length,mat[0].length);
        
        double pca_mat[][] = U.getArrayCopy();
        print_matrix(pca_mat,"PCA matrix");
        try{
            save_matrix(pca_mat,"PCA matrix");
        }catch(IOException e){
            System.err.print(e);
        }
        
        //Computing determinant of matrix
        //double a = matrix_determinant(mat, mat.length);
        //System.out.print(a);
        
    }

    /*Mean Calculation*/
    public double[] mean(double matm[][]) {
        int mu_row = matm.length;
        int mu_col = matm[0].length;
        System.out.println("Computing mean of variables of matrix: " + mu_row + " x " + mu_col);
        double mu[] = new double[mu_col];
        double temp[] = new double[mu_col];

        for (int j = 0; j < mu_col; j++) {
            temp[j] = 0.0;
        }//initilization of temp to zero
        for (int j = 0; j < mu_col; j++) {//j columns
            for (int i = 0; i < mu_row; i++) {//i rows
                //System.out.print(" "+mat[j][i]);  
                temp[j] = temp[j] + matm[i][j];
            }
            temp[j] = temp[j] / ((double) mu_row);
            System.out.printf("mu %d:  %.3f\n", j, temp[j]);
        }
        //copy temp to mu
        System.arraycopy(temp, 0, mu, 0, mu_col); //System.out.println(" "+mu[i]);

        return mu;
    }

    /*Varience Calculation*/
    public double[] sigma2(double matv[][], double mv[]) {
        int sigma_row = matv.length;
        int sigma_col = matv[0].length;
        System.out.println("Computing varience of variables of matrix: " + sigma_row + " x " + sigma_col);
        double sigma2[] = new double[sigma_col];
        double temp[] = new double[sigma_col];
        for (int j = 0; j < sigma_col; j++) {
            temp[j] = 0.0;
        }//initilization of temp to zero
        for (int j = 0; j < sigma_col; j++) {//j columns
            for (int i = 0; i < sigma_row; i++) {//i rows
                //System.out.print(" "+mat[j][i]); 
                temp[j] = temp[j] + (matv[i][j] - mv[j]) * (matv[i][j] - mv[j]);
            }
            temp[j] = temp[j] / ((double) sigma_row);//for proper use of sigma2 do not use -1 
            //System.out.println(" >sigma2"+i+": "+temp[i]);       
        }
        //copy tem to sigma2
        System.arraycopy(temp, 0, sigma2, 0, sigma_col); //System.out.println(" "+sigma2[i]);

        return sigma2;
    }

    /*Coumuting x - mean*/
    public double[][] x_minus_mean(double matxm[][], double mean[]) {
        int xm_row = matxm.length;
        int xm_col = matxm[0].length;
        System.out.println("Subtracting mean [1 x " + mean.length + "] from crossvalidation matrix: " + xm_row + " x " + xm_col);
        double x_m[][] = new double[xm_row][xm_col];
        for (int i = 0; i < xm_row; i++) {//i rows
            for (int j = 0; j < xm_col; j++) {// j columns
                x_m[i][j] = matxm[i][j] - mean[j];
            }
        }
        return x_m;
    }

    /*Computing covarience matrix*/
    public double[][] sigmacovar(double x_mean[][]) {
        double[][] X_meanT = matrix_transpose(x_mean);
        double[][] X_mul = matrix_multiplication(X_meanT, x_mean);
        double[][] X_by_N = matrix_by_N(X_mul, x_mean.length);

        return X_by_N;
    }

    /* Transpose of a matric*/
    public double[][] matrix_transpose(double matrix[][]) {
        int row = matrix.length;//row of mat 1
        int col = matrix[0].length;//column of mat 1
        double[][] mtarixT = new double[col][row]; //row become column and vice versa

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                mtarixT[j][i] = matrix[i][j];
            }
        }
        return mtarixT;
    }

    /* This method find the multiplication of two Matrix */
    public double[][] matrix_multiplication(double mat1[][], double mat2[][]) {
        int row1 = mat1.length;//row of mat 1
        int col1 = mat1[0].length;//column of mat 1
        int row2 = mat2.length; //row of mat 2
        int col2 = mat2[0].length;//col of mat 2

        System.out.println("Multiplying matrix X-mu [" + row1 + " x " + col1 + "] with Inverse of covarience matrix [" + row2 + " x " + col2 + "]");
        System.out.println("Resultant matrix is: [" + row1 + " x " + col2 + "]");
        //if column of mat 1 is equal to row of mat 2 multiplication possible else not 
        //and resultent matrix will be of size row of mat 1 and col of mat 2
        double mat_mul_res[][] = new double[row1][col2];

        if (col1 == row2) {
            for (int i = 0; i < row1; i++) {
                for (int j = 0; j < col2; j++) {
                    mat_mul_res[i][j] = 0;
                    for (int k = 0; k < col1; k++) {
                        mat_mul_res[i][j] += mat1[i][k] * mat2[k][j];
                    }
                }
            }
        } else {
            System.out.printf("\n Multiplication is not possible");
        }
        return mat_mul_res;
    }

    private double[][] matrix_by_N(double[][] matrix, int N) {
        int row = matrix.length;
        int col = matrix[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = matrix[i][j] / (N - 1.0);
            }
        }
        return matrix;
    }

    /* find determinant of a square matrix */
    public double matrix_determinant(double A[][], int N) {
        //double det = 0.0;
        double det;
        if(A.length != A[0].length){
            System.out.print("Input a square matrix only: ");
            return 0.0;
        }

        switch (N) {
            case 1://Simple calculation
                det = A[0][0];
                break;
            case 2: //Simple calculation for 2 x 2 matrix
                det = A[0][0] * A[1][1] - A[1][0] * A[0][1];
                break;
            default://More than 2 x 2 matrix need to be decomposed
                det = 0;
                for (int j1 = 0; j1 < N; j1++) {
                    m = new double[N - 1][];
                    
                    for (int k = 0; k < (N - 1); k++) {
                        m[k] = new double[N - 1];
                    }
                    for (int i = 1; i < N; i++) {
                        int j2 = 0;
                        for (int j = 0; j < N; j++) {
                            if (j == j1) {
                                continue;
                            }
                            m[i - 1][j2] = A[i][j];
                            j2++;
                        }
                    }
                    det += Math.pow(-1.0, 1.0 + j1 + 1.0) * A[0][j1] * matrix_determinant(m, N - 1);
                }   break;
        }
        return det;
    }

    private void readData() {
        String fileName = "";

        //read training data
        fileName = data_file;
        System.out.println("Reading data file: " + data_file);
        mat = readDataFromFile(fileName);
        int row = mat.length;
        int col = mat[0].length;
        System.out.println("Compleated.");
    }

    private double[][] readDataFromFile(String fileName) {
        int rowlength = 0;
        int collength = 0;
        double[][] data = null;
        String absolutePath = filePath + fileName;
        try {
            FileReader fin = new FileReader(absolutePath);//readt output Train to make ensemble
            BufferedReader br = new BufferedReader(fin);
            String line;
            String[] tokens;
            rowlength = 0;
            if ((line = br.readLine()) != null) {
                tokens = line.split(",");
                collength = tokens.length;
            }
            while ((line = br.readLine()) != null) {
                rowlength++;
            }
            rowlength++;//count the last row

            System.out.println("    The file has " + rowlength + " samples");
            System.out.println("    Each sample has a leangth " + collength);

            data = new double[rowlength][collength];

            br.close();
            fin.close();

            FileReader fin1 = new FileReader(absolutePath);//readt output Train to make ensemble
            BufferedReader br1 = new BufferedReader(fin1);

            System.out.println("Data:");
            for (int i = 0; i < rowlength; i++) {
                line = br1.readLine();
                tokens = line.split(",");
                for (int j = 0; j < collength; j++) {
                    data[i][j] = Double.parseDouble(tokens[j]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Errot in data reading" + e);
        }
        return data;
    }

    void print_matrix(double matrix[][], String msg) {
        int rowlength = matrix.length;
        int collength = matrix[0].length;
        System.out.println("Printing matrix of size: "+rowlength+" x "+ collength);
        System.out.println(msg + ":");
        for (int i = 0; i < rowlength; i++) {
            System.out.print("|");
            for (int j = 0; j < collength; j++) {
                if (matrix[i][j] < 0) {
                    System.out.printf("  %.5f", matrix[i][j]);
                } else {
                    System.out.printf("   %.5f", matrix[i][j]);
                }
            }
            System.out.print(" |\n");
        }
    }
    
    
    void save_matrix(double matrix[][], String msg) throws IOException {
        int rowlength = matrix.length;
        int collength = matrix[0].length;
        System.out.println("Saving matrix of size: "+rowlength+" x "+ collength);
        System.out.println(msg + ":");
        
        try (FileWriter fw = new FileWriter("pca_"+data_file); 
                BufferedWriter bw = new BufferedWriter(fw); 
                PrintWriter pr = new PrintWriter(bw)) {
            for (int i = 0; i < rowlength; i++) {
                for (int j = 0; j < collength; j++) {
                    pr.print(matrix[i][j]+",");
                }
                pr.println();
            }
            System.out.print("Saved");
        }
    }

}
