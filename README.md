# Principal Component Analysis
Principal Component Analysis Java Code.

*The program uses Jama library to compute singular value decomposition. The covariance matrix, however, was implemented.

PCA algorithm in a nutshell:

X - a matrix of dimension n x m 

Compute coverience matrix<br>
C - covariance matrix of X

Perform singular value decompostion of C: http://web.mit.edu/be.400/www/SVD/Singular_Value_Decomposition.htm <br>
[U,S,V] = SVD(C)

Matrix U holds m eigenvectors (Principal componenets) of X<br>
Fetch required number of components from U


---
input: This program takes a "file_name.csv" file as an input

output: This program produces a "pca_file_name.csv" as an output 

---

The code also contains:<br>
Matrix transpose calculator <br>
Matrix multiplication calculator <br>
Matrix variance calculator <br>
Matrix determinant calculator <br>
