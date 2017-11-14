# Principal Component Analysis
Principal Component Analysis Java Code.

*The program uses Jama library to compute singular value decomposition. The covariance matrix, however, was implemented.

PCA algorithm in a nutshell:

X - a matrix of dimension n x m 

Compute coverience matrix
C - covariance matrix of X
Perform singular value decompostion: http://web.mit.edu/be.400/www/SVD/Singular_Value_Decomposition.htm
[USV] = SVD(X)

Matrix U holds m eigenvector of X
Fetch number of component  as required
