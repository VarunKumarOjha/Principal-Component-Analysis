/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pca;

/**
 *
 * @author vojha
 */
public class Eigenvector {

    static final double epsilon = 0.0000001;     // convergency threshold

    public static void eigenvectors(double A[][]){
        int length = A.length;

    }
    
    /* Power Iteration Method for a single eigenvector (prime) computation*/
    public static void eigenvector(double A[][]){
        int length = A.length;
        double [] p = new double[length];
        for (int i = 0; i < p.length; i++) {
            p[i] = 1;
        }
        
        double[] q = new double[length];
        double lambda = 0.0;
        
        System.out.println();
        for (int i = 0; i < p.length; i++) {
            System.out.print("p[" + i + "]\t\t");
        }
        System.out.println("lambda");
        
        do {
            for (int i = 0; i < p.length; i++) {
                System.out.format("%f \t", p[i]);  // show the current vector
            }
            System.out.format("%f \n", lambda); // and the eigenvalue (lambda)

            q = p;
            p = AxP(A, q);
            lambda = norm(p); 
            p = PxL(p, 1 / lambda);

        } while (norm(PminusQ(p, q)) > epsilon);
    }
    
    public static void main(String[] args) {
        //static double lambda;                   // eigenvalue
        double[][] a = {{3, 1, 0}, // transposed adjaceny matrix
                                {0, 3, 1}, // of the social network graph
                                {0, 0, 3},};
        
        double[][] A = {{5, 4, 6, 7}, 
                        {8, 1, 2, 6}, 
                        {3, 4, 6, 2}, 
                        {7, 8, 9, 4}};
        //static double[] p = {1, 1, 1};            // initial eigenvector
        eigenvector(A);
        
//        double[] q;
//
//        System.out.println();
//        for (int i = 0; i < p.length; i++) {
//            System.out.print("p[" + i + "]\t\t");
//        }
//        System.out.println("lambda");
//
//        do {
//            for (int i = 0; i < p.length; i++) {
//                System.out.format("%f \t", p[i]);  // show the current vector
//            }
//            System.out.format("%f \n", lambda); // and the eigenvalue (lambda)
//
//            q = p;
//            p = AxP(a, q);
//            lambda = norm(p);
//            p = PxL(p, 1 / lambda);
//
//        } while (norm(PminusQ(p, q)) > epsilon);
    }

// Computes P = A x P (A - matrix, P - column vector)
    public static double[] AxP(double[][] a, double[] p) {
        double[] q = new double[p.length];
        double s;
        for (int i = 0; i < p.length; i++) {
            q[i] = 0;
            for (int j = 0; j < p.length; j++) {
                q[i] = q[i] + a[i][j] * p[j];
            }
        }
        return q;
    }

// Computes P = P x L (P - vector, L - scalar)    
    public static double[] PxL(double[] p, double lambda) {
        double[] q = new double[p.length];
        for (int i = 0; i < p.length; i++) {
            q[i] = p[i] * lambda;
        }
        return q;
    }

// Computes P-Q (P and Q - vectors)    
    public static double[] PminusQ(double[] p, double[] q) {
        double[] r = new double[p.length];
        for (int i = 0; i < p.length; i++) {
            r[i] = p[i] - q[i];
        }
        return r;
    }

// Computes Euclidean norm of P
    public static double norm(double[] p) {
        double s = 0;
        for (int i = 0; i < p.length; i++) {
            s = s + p[i] * p[i];
        }
        return Math.sqrt(s);
    }
}
