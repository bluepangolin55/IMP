/*
 * Copyright (c) 2009-2015, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Efficient Java Matrix Library (EJML).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ejml.ops;

import org.ejml.EjmlParameters;
import org.ejml.alg.dense.misc.*;
import org.ejml.alg.dense.mult.MatrixMatrixMult32;
import org.ejml.alg.dense.mult.MatrixMultProduct32;
import org.ejml.alg.dense.mult.MatrixVectorMult32;
import org.ejml.alg.dense.mult.VectorVectorMult32;
import org.ejml.data.D1Matrix32F;
import org.ejml.data.DenseMatrix32F;
import org.ejml.data.RealMatrix32F;

import java.util.Arrays;

/**
 * <p>
 * Common matrix operations are contained here.  Which specific underlying algorithm is used
 * is not specified just the out come of the operation.  Nor should calls to these functions
 * reply on the underlying implementation.  Which algorithm is used can depend on the matrix
 * being passed in.
 * </p>
 * <p>
 * For more exotic and specialized generic operations see {@link org.ejml.ops.SpecializedOps}.
 * </p>
 * @see org.ejml.alg.dense.mult.MatrixMatrixMult32
 * @see org.ejml.alg.dense.mult.MatrixVectorMult32
 * @see org.ejml.ops.SpecializedOps
 * @see org.ejml.ops.MatrixFeatures
 *
 * @author Peter Abeles
 */
@SuppressWarnings({"ForLoopReplaceableByForEach"})
public class CommonOps32 extends CommonOps{
    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = a * b <br>
     * <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void mult( D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        if( b.numCols == 1 ) {
            MatrixVectorMult32.mult(a,b,c);
        } else if( b.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ) {
            MatrixMatrixMult32.mult_reorder(a,b,c);
        } else {
            MatrixMatrixMult32.mult_small(a,b,c);
        }
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = &alpha; * a * b <br>
     * <br>
     * c<sub>ij</sub> = &alpha; &sum;<sub>k=1:n</sub> { * a<sub>ik</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param alpha Scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void mult( float alpha , D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        // TODO add a matrix vectory multiply here
        if( b.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ) {
            MatrixMatrixMult32.mult_reorder(alpha,a,b,c);
        } else {
            MatrixMatrixMult32.mult_small(alpha,a,b,c);
        }
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = a<sup>T</sup> * b <br>
     * <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multTransA( D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        if( b.numCols == 1 ) {
            // todo check a.numCols == 1 and do inner product?
            // there are significantly faster algorithms when dealing with vectors
            if( a.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ) {
                MatrixVectorMult32.multTransA_reorder(a,b,c);
            } else {
                MatrixVectorMult32.multTransA_small(a,b,c);
            }
        } else if( a.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ||
                b.numCols >= EjmlParameters.MULT_COLUMN_SWITCH  ) {
            MatrixMatrixMult32.multTransA_reorder(a,b,c);
        } else {
            MatrixMatrixMult32.multTransA_small(a,b,c);
        }
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = &alpha; * a<sup>T</sup> * b <br>
     * <br>
     * c<sub>ij</sub> = &alpha; &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param alpha Scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multTransA( float alpha , D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        // TODO add a matrix vectory multiply here
        if( a.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ||
                b.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ) {
            MatrixMatrixMult32.multTransA_reorder(alpha,a,b,c);
        } else {
            MatrixMatrixMult32.multTransA_small(alpha,a,b,c);
        }
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = a * b<sup>T</sup> <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multTransB( D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        if( b.numRows == 1 ) {
            MatrixVectorMult32.mult(a,b,c);
        } else {
            MatrixMatrixMult32.multTransB(a,b,c);
        }
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c =  &alpha; * a * b<sup>T</sup> <br>
     * c<sub>ij</sub> = &alpha; &sum;<sub>k=1:n</sub> {  a<sub>ik</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param alpha Scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multTransB( float alpha , D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        // TODO add a matrix vectory multiply here
        MatrixMatrixMult32.multTransB(alpha,a,b,c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = a<sup>T</sup> * b<sup>T</sup><br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multTransAB( D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        if( b.numRows == 1) {
            // there are significantly faster algorithms when dealing with vectors
            if( a.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ) {
                MatrixVectorMult32.multTransA_reorder(a,b,c);
            } else {
                MatrixVectorMult32.multTransA_small(a,b,c);
            }
        } else if( a.numCols >= EjmlParameters.MULT_TRANAB_COLUMN_SWITCH ) {
            MatrixMatrixMult32.multTransAB_aux(a,b,c,null);
        } else {
            MatrixMatrixMult32.multTransAB(a,b,c);
        }
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = &alpha; * a<sup>T</sup> * b<sup>T</sup><br>
     * c<sub>ij</sub> = &alpha; &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param alpha Scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multTransAB( float alpha , D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        // TODO add a matrix vectory multiply here
        if( a.numCols >= EjmlParameters.MULT_TRANAB_COLUMN_SWITCH ) {
            MatrixMatrixMult32.multTransAB_aux(alpha,a,b,c,null);
        } else {
            MatrixMatrixMult32.multTransAB(alpha,a,b,c);
        }
    }

    /**
     * <p></p>
     * Computes the dot product or inner product between two vectors.  If the two vectors are columns vectors
     * then it is defined as:<br>
     * {@code dot(a,b) = a<sup>T</sup> * b}<br>
     * If the vectors are column or row or both is ignored by this function.
     * </p>
     * @param a Vector
     * @param b Vector
     * @return Dot product of the two vectors
     */
    public static float dot( D1Matrix32F a , D1Matrix32F b ) {
        if( !MatrixFeatures.isVector(a) || !MatrixFeatures.isVector(b))
            throw new RuntimeException("Both inputs must be vectors");

        return VectorVectorMult32.innerProd(a,b);
    }

    /**
     * <p>Computes the matrix multiplication inner product:<br>
     * <br>
     * c = a<sup>T</sup> * a <br>
     * <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * a<sub>kj</sub>}
     * </p>
     * 
     * <p>
     * Is faster than using a generic matrix multiplication by taking advantage of symmetry.  For
     * vectors there is an even faster option, see {@link org.ejml.alg.dense.mult.VectorVectorMult32#innerProd(org.ejml.data.D1Matrix32F, org.ejml.data.D1Matrix32F)}
     * </p>
     *
     * @param a The matrix being multiplied. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multInner( D1Matrix32F a , D1Matrix32F c )
    {
        if( a.numCols != c.numCols || a.numCols != c.numRows )
            throw new IllegalArgumentException("Rows and columns of 'c' must be the same as the columns in 'a'");
        
        if( a.numCols >= EjmlParameters.MULT_INNER_SWITCH ) {
            MatrixMultProduct32.inner_small(a, c);
        } else {
            MatrixMultProduct32.inner_reorder(a, c);
        }
    }

    /**
     * <p>Computes the matrix multiplication outer product:<br>
     * <br>
     * c = a * a<sup>T</sup> <br>
     * <br>
     * c<sub>ij</sub> = &sum;<sub>k=1:m</sub> { a<sub>ik</sub> * a<sub>jk</sub>}
     * </p>
     *
     * <p>
     * Is faster than using a generic matrix multiplication by taking advantage of symmetry.
     * </p>
     *
     * @param a The matrix being multiplied. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multOuter( D1Matrix32F a , D1Matrix32F c )
    {
        if( a.numRows != c.numCols || a.numRows != c.numRows )
            throw new IllegalArgumentException("Rows and columns of 'c' must be the same as the rows in 'a'");

        MatrixMultProduct32.outer(a, c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + a * b<br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAdd( D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        if( b.numCols == 1 ) {
            MatrixVectorMult32.multAdd(a,b,c);
        } else {
            if( b.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ) {
                MatrixMatrixMult32.multAdd_reorder(a,b,c);
            } else {
                MatrixMatrixMult32.multAdd_small(a,b,c);
            }
        }
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + &alpha; * a * b<br>
     * c<sub>ij</sub> = c<sub>ij</sub> +  &alpha; * &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param alpha scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAdd( float alpha , D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        // TODO add a matrix vectory multiply here
        if( b.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ) {
            MatrixMatrixMult32.multAdd_reorder(alpha,a,b,c);
        } else {
            MatrixMatrixMult32.multAdd_small(alpha,a,b,c);
        }
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + a<sup>T</sup> * b<br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransA( D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        if( b.numCols == 1 ) {
            if( a.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ) {
                MatrixVectorMult32.multAddTransA_reorder(a,b,c);
            } else {
                MatrixVectorMult32.multAddTransA_small(a,b,c);
            }
        } else {
            if( a.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ||
                    b.numCols >= EjmlParameters.MULT_COLUMN_SWITCH  ) {
                MatrixMatrixMult32.multAddTransA_reorder(a,b,c);
            } else {
                MatrixMatrixMult32.multAddTransA_small(a,b,c);
            }
        }
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + &alpha; * a<sup>T</sup> * b<br>
     * c<sub>ij</sub> =c<sub>ij</sub> +  &alpha; * &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>kj</sub>}
     * </p>
     *
     * @param alpha scaling factor
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransA( float alpha , D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        // TODO add a matrix vectory multiply here
        if( a.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ||
                b.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ) {
            MatrixMatrixMult32.multAddTransA_reorder(alpha,a,b,c);
        } else {
            MatrixMatrixMult32.multAddTransA_small(alpha,a,b,c);
        }
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + a * b<sup>T</sup> <br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransB( D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        MatrixMatrixMult32.multAddTransB(a,b,c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + &alpha; * a * b<sup>T</sup><br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &alpha; * &sum;<sub>k=1:n</sub> { a<sub>ik</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param alpha Scaling factor.
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransB( float alpha , D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        // TODO add a matrix vectory multiply here
        MatrixMatrixMult32.multAddTransB(alpha,a,b,c);
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + a<sup>T</sup> * b<sup>T</sup><br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param a The left matrix in the multiplication operation. Not Modified.
     * @param b The right matrix in the multiplication operation. Not Modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransAB( D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        if( b.numRows == 1 ) {
            // there are significantly faster algorithms when dealing with vectors
            if( a.numCols >= EjmlParameters.MULT_COLUMN_SWITCH ) {
                MatrixVectorMult32.multAddTransA_reorder(a,b,c);
            } else {
                MatrixVectorMult32.multAddTransA_small(a,b,c);
            }
        } else if( a.numCols >= EjmlParameters.MULT_TRANAB_COLUMN_SWITCH ) {
            MatrixMatrixMult32.multAddTransAB_aux(a,b,c,null);
        } else {
            MatrixMatrixMult32.multAddTransAB(a,b,c);
        }
    }

    /**
     * <p>
     * Performs the following operation:<br>
     * <br>
     * c = c + &alpha; * a<sup>T</sup> * b<sup>T</sup><br>
     * c<sub>ij</sub> = c<sub>ij</sub> + &alpha; * &sum;<sub>k=1:n</sub> { a<sub>ki</sub> * b<sub>jk</sub>}
     * </p>
     *
     * @param alpha Scaling factor.
     * @param a The left matrix in the multiplication operation. Not Modified.
     * @param b The right matrix in the multiplication operation. Not Modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void multAddTransAB( float alpha , D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        // TODO add a matrix vectory multiply here
        if( a.numCols >= EjmlParameters.MULT_TRANAB_COLUMN_SWITCH ) {
            MatrixMatrixMult32.multAddTransAB_aux(alpha,a,b,c,null);
        } else {
            MatrixMatrixMult32.multAddTransAB(alpha,a,b,c);
        }
    }

 

    /**
     * <p>Performs an "in-place" transpose.</p>
     *
     * <p>
     * For square matrices the transpose is truly in-place and does not require
     * additional memory.  For non-square matrices, internally a temporary matrix is declared and
     * {@link #transpose(org.ejml.data.DenseMatrix32F, org.ejml.data.DenseMatrix32F)} is invoked.
     * </p>
     *
     * @param mat The matrix that is to be transposed. Modified.
     */
    public static void transpose( DenseMatrix32F mat ) {
        if( mat.numCols == mat.numRows ){
            TransposeAlgs32.square(mat);
        } else {
            DenseMatrix32F b = new DenseMatrix32F(mat.numCols,mat.numRows);
            transpose(mat,b);
            mat.set(b);
        }
    }

    /**
     * <p>
     * Transposes matrix 'a' and stores the results in 'b':<br>
     * <br>
     * b<sub>ij</sub> = a<sub>ji</sub><br>
     * where 'b' is the transpose of 'a'.
     * </p>
     *
     * @param A The original matrix.  Not modified.
     * @param A_tran Where the transpose is stored. If null a new matrix is created. Modified.
     * @return The transposed matrix.
     */
    public static DenseMatrix32F transpose( DenseMatrix32F A, DenseMatrix32F A_tran)
    {
        if( A_tran == null ) {
            A_tran = new DenseMatrix32F(A.numCols,A.numRows);
        } else {
            if( A.numRows != A_tran.numCols || A.numCols != A_tran.numRows ) {
                throw new IllegalArgumentException("Incompatible matrix dimensions");
            }
        }

        if( A.numRows > EjmlParameters.TRANSPOSE_SWITCH &&
                A.numCols > EjmlParameters.TRANSPOSE_SWITCH )
            TransposeAlgs32.block(A,A_tran,EjmlParameters.BLOCK_WIDTH);
        else
            TransposeAlgs32.standard(A,A_tran);

        return A_tran;
    }


    /**
     * <p>
     * This computes the trace of the matrix:<br>
     * <br>
     * trace = &sum;<sub>i=1:n</sub> { a<sub>ii</sub> }<br>
     * where n = min(numRows,numCols)
     * </p>
     *
     * @param a A square matrix.  Not modified.
     */
    public static float trace( D1Matrix32F a ) {
        int N = Math.min(a.numRows,a.numCols);
        float sum = 0;
        int index = 0;
        for( int i = 0; i < N; i++ ) {
            sum += a.get(index);
            index += 1 + a.numCols;
        }

        return sum;
    }


    /**
     * Converts the rows in a matrix into a set of vectors.
     *
     * @param A Matrix.  Not modified.
     * @param v
     * @return An array of vectors.
     */
    public static DenseMatrix32F[] rowsToVector(DenseMatrix32F A, DenseMatrix32F[] v)
    {
        DenseMatrix32F []ret;
        if( v == null || v.length < A.numRows ) {
            ret = new DenseMatrix32F[ A.numRows ];
        } else {
            ret = v;
        }


        for( int i = 0; i < ret.length; i++ ) {
            if( ret[i] == null ) {
                ret[i] = new DenseMatrix32F(A.numCols,1);
            } else {
                ret[i].reshape(A.numCols,1, false);
            }

            DenseMatrix32F u = ret[i];

            for( int j = 0; j < A.numCols; j++ ) {
                u.set(j,0, A.get(i,j));
            }
        }

        return ret;
    }

    /**
     * Sets all the diagonal elements equal to one and everything else equal to zero.
     * If this is a square matrix then it will be an identity matrix.
     *
     * @see #identity(int)
     *
     * @param mat A square matrix.
     */
    public static void setIdentity( D1Matrix32F mat )
    {
        int width = mat.numRows < mat.numCols ? mat.numRows : mat.numCols;

        Arrays.fill(mat.data,0,mat.getNumElements(),0);

        int index = 0;
        for( int i = 0; i < width; i++ , index += mat.numCols + 1) {
            mat.data[index] = 1;
        }
    }



    /**
     * <p>
     * Creates a new square matrix whose diagonal elements are specified by diagEl and all
     * the other elements are zero.<br>
     * <br>
     * a<sub>ij</sub> = 0         if i &le; j<br>
     * a<sub>ij</sub> = diag[i]   if i = j<br>
     * </p>
     *
     * @see #diagR
     *
     * @param diagEl Contains the values of the diagonal elements of the resulting matrix.
     * @return A new matrix.
     */
    public static DenseMatrix32F diag( float ...diagEl )
    {
        return diag(null,diagEl.length,diagEl);
    }

    /**
     * @see #diag(float...)
     */
    public static DenseMatrix32F diag( DenseMatrix32F ret , int width , float ...diagEl )
    {
        if( ret == null ) {
            ret = new DenseMatrix32F(width,width);
        } else {
            if( ret.numRows != width || ret.numCols != width )
                throw new IllegalArgumentException("Unexpected matrix size");

            CommonOps32.fill(ret, 0);
        }

        for( int i = 0; i < width; i++ ) {
            ret.unsafe_set(i, i, diagEl[i]);
        }

        return ret;
    }

    /**
     * <p>
     * Creates a new rectangular matrix whose diagonal elements are specified by diagEl and all
     * the other elements are zero.<br>
     * <br>
     * a<sub>ij</sub> = 0         if i &le; j<br>
     * a<sub>ij</sub> = diag[i]   if i = j<br>
     * </p>
     *
     * @see #diag
     *
     * @param numRows Number of rows in the matrix.
     * @param numCols Number of columns in the matrix.
     * @param diagEl Contains the values of the diagonal elements of the resulting matrix.
     * @return A new matrix.
     */
    public static DenseMatrix32F diagR( int numRows , int numCols , float ...diagEl )
    {
        DenseMatrix32F ret = new DenseMatrix32F(numRows,numCols);

        int o = Math.min(numRows,numCols);

        for( int i = 0; i < o; i++ ) {
            ret.set(i,i,diagEl[i]);
        }

        return ret;
    }

 

    /**
     * <p>
     * Extracts a submatrix from 'src' and inserts it in a submatrix in 'dst'.
     * </p>
     * <p>
     * s<sub>i-y0 , j-x0</sub> = o<sub>ij</sub> for all y0 &le; i < y1 and x0 &le; j < x1 <br>
     * <br>
     * where 's<sub>ij</sub>' is an element in the submatrix and 'o<sub>ij</sub>' is an element in the
     * original matrix.
     * </p>
     *
     * @param src The original matrix which is to be copied.  Not modified.
     * @param srcX0 Start column.
     * @param srcX1 Stop column+1.
     * @param srcY0 Start row.
     * @param srcY1 Stop row+1.
     * @param dst Where the submatrix are stored.  Modified.
     * @param dstY0 Start row in dst.
     * @param dstX0 start column in dst.
     */
    public static void extract( RealMatrix32F src,
                                int srcY0, int srcY1,
                                int srcX0, int srcX1,
                                RealMatrix32F dst ,
                                int dstY0, int dstX0 )
    {
        if( srcY1 < srcY0 || srcY0 < 0 || srcY1 > src.getNumRows() )
            throw new IllegalArgumentException("srcY1 < srcY0 || srcY0 < 0 || srcY1 > src.numRows");
        if( srcX1 < srcX0 || srcX0 < 0 || srcX1 > src.getNumCols() )
            throw new IllegalArgumentException("srcX1 < srcX0 || srcX0 < 0 || srcX1 > src.numCols");

        int w = srcX1-srcX0;
        int h = srcY1-srcY0;

        if( dstY0+h > dst.getNumRows() )
            throw new IllegalArgumentException("dst is too small in rows");
        if( dstX0+w > dst.getNumCols() )
            throw new IllegalArgumentException("dst is too small in columns");

        // interestingly, the performance is only different for small matrices but identical for larger ones
        if( src instanceof DenseMatrix32F && dst instanceof DenseMatrix32F ) {
            ImplCommonOps_DenseMatrix32F.extract((DenseMatrix32F)src,srcY0,srcX0,(DenseMatrix32F)dst,dstY0,dstX0, h, w);
        } else {
            ImplCommonOps_Matrix32F.extract(src,srcY0,srcX0,dst,dstY0,dstX0, h, w);
        }
    }

    /**
     * <p>
     * Creates a new matrix which is the specified submatrix of 'src'
     * </p>
     * <p>
     * s<sub>i-y0 , j-x0</sub> = o<sub>ij</sub> for all y0 &le; i < y1 and x0 &le; j < x1 <br>
     * <br>
     * where 's<sub>ij</sub>' is an element in the submatrix and 'o<sub>ij</sub>' is an element in the
     * original matrix.
     * </p>
     *
     * @param src The original matrix which is to be copied.  Not modified.
     * @param srcX0 Start column.
     * @param srcX1 Stop column+1.
     * @param srcY0 Start row.
     * @param srcY1 Stop row+1.
     * @return Extracted submatrix.
     */
    public static DenseMatrix32F extract( DenseMatrix32F src,
                                          int srcY0, int srcY1,
                                          int srcX0, int srcX1 )
    {
        if( srcY1 <= srcY0 || srcY0 < 0 || srcY1 > src.numRows )
            throw new IllegalArgumentException("srcY1 <= srcY0 || srcY0 < 0 || srcY1 > src.numRows");
        if( srcX1 <= srcX0 || srcX0 < 0 || srcX1 > src.numCols )
            throw new IllegalArgumentException("srcX1 <= srcX0 || srcX0 < 0 || srcX1 > src.numCols");

        int w = srcX1-srcX0;
        int h = srcY1-srcY0;

        DenseMatrix32F dst = new DenseMatrix32F(h,w);

        ImplCommonOps_DenseMatrix32F.extract(src,srcY0,srcX0,dst,0,0, h, w);

        return dst;
    }

    /**
     * <p>
     * Extracts the diagonal elements 'src' write it to the 'dst' vector.  'dst'
     * can either be a row or column vector.
     * <p>
     *
     * @param src Matrix whose diagonal elements are being extracted. Not modified.
     * @param dst A vector the results will be written into. Modified.
     */
    public static void extractDiag( DenseMatrix32F src, DenseMatrix32F dst )
    {
        int N = Math.min(src.numRows, src.numCols);

        if( !MatrixFeatures.isVector(dst) ) {
            throw new IllegalArgumentException("Expected a vector for dst.");
        } else if( dst.getNumElements() != N ) {
            throw new IllegalArgumentException("Expected "+N+" elements in dst.");
        }

        for( int i = 0; i < N; i++ ) {
            dst.set( i , src.unsafe_get(i,i) );
        }
    }

    /**
     * Extracts the row from a matrix.
     * @param a Input matrix
     * @param row Which row is to be extracted
     * @param out output. Storage for the extracted row. If null then a new vector will be returned.
     * @return The extracted row.
     */
    public static DenseMatrix32F extractRow( DenseMatrix32F a , int row , DenseMatrix32F out ) {
        if( out == null)
            out = new DenseMatrix32F(1,a.numCols);
        else if( !MatrixFeatures.isVector(out) || out.getNumElements() != a.numCols )
            throw new IllegalArgumentException("Output must be a vector of length "+a.numCols);

        System.arraycopy(a.data,a.getIndex(row,0),out.data,0,a.numCols);

        return out;
    }

    /**
     * Extracts the column from a matrix.
     * @param a Input matrix
     * @param column Which column is to be extracted
     * @param out output. Storage for the extracted column. If null then a new vector will be returned.
     * @return The extracted column.
     */
    public static DenseMatrix32F extractColumn( DenseMatrix32F a , int column , DenseMatrix32F out ) {
        if( out == null)
            out = new DenseMatrix32F(a.numRows,1);
        else if( !MatrixFeatures.isVector(out) || out.getNumElements() != a.numRows )
            throw new IllegalArgumentException("Output must be a vector of length "+a.numRows);

        int index = column;
        for (int i = 0; i < a.numRows; i++, index += a.numCols ) {
            out.data[i] = a.data[index];
        }
        return out;
    }

    /**
     * Inserts matrix 'src' into matrix 'dest' with the (0,0) of src at (row,col) in dest.
     * This is equivalent to calling extract(src,0,src.numRows,0,src.numCols,dest,destY0,destX0).
     *
     * @param src matrix that is being copied into dest. Not modified.
     * @param dest Where src is being copied into. Modified.
     * @param destY0 Start row for the copy into dest.
     * @param destX0 Start column for the copy into dest.
     */
    public static void insert( RealMatrix32F src, RealMatrix32F dest, int destY0, int destX0) {
        extract(src,0,src.getNumRows(),0,src.getNumCols(),dest,destY0,destX0);
    }

    /**
     * <p>
     * Returns the value of the element in the matrix that has the largest value.<br>
     * <br>
     * Max{ a<sub>ij</sub> } for all i and j<br>
     * </p>
     *
     * @param a A matrix. Not modified.
     * @return The max element value of the matrix.
     */
    public static float elementMax( D1Matrix32F a ) {
        final int size = a.getNumElements();

        float max = a.get(0);
        for( int i = 1; i < size; i++ ) {
            float val = a.get(i);
            if( val >= max ) {
                max = val;
            }
        }

        return max;
    }

    /**
     * <p>
     * Returns the absolute value of the element in the matrix that has the largest absolute value.<br>
     * <br>
     * Max{ |a<sub>ij</sub>| } for all i and j<br>
     * </p>
     *
     * @param a A matrix. Not modified.
     * @return The max abs element value of the matrix.
     */
    public static float elementMaxAbs( D1Matrix32F a ) {
        final int size = a.getNumElements();

        float max = 0;
        for( int i = 0; i < size; i++ ) {
            float val = Math.abs(a.get( i ));
            if( val > max ) {
                max = val;
            }
        }

        return max;
    }

    /**
     * <p>
     * Returns the value of the element in the matrix that has the minimum value.<br>
     * <br>
     * Min{ a<sub>ij</sub> } for all i and j<br>
     * </p>
     *
     * @param a A matrix. Not modified.
     * @return The value of element in the matrix with the minimum value.
     */
    public static float elementMin( D1Matrix32F a ) {
        final int size = a.getNumElements();

        float min = a.get(0);
        for( int i = 1; i < size; i++ ) {
            float val = a.get(i);
            if( val < min ) {
                min = val;
            }
        }

        return min;
    }

    /**
     * <p>
     * Returns the absolute value of the element in the matrix that has the smallest absolute value.<br>
     * <br>
     * Min{ |a<sub>ij</sub>| } for all i and j<br>
     * </p>
     *
     * @param a A matrix. Not modified.
     * @return The max element value of the matrix.
     */
    public static float elementMinAbs( D1Matrix32F a ) {
        final int size = a.getNumElements();

        float min = Float.MAX_VALUE;
        for( int i = 0; i < size; i++ ) {
            float val = Math.abs(a.get(i));
            if( val < min ) {
                min = val;
            }
        }

        return min;
    }

    /**
     * <p>Performs the an element by element multiplication operation:<br>
     * <br>
     * a<sub>ij</sub> = a<sub>ij</sub> * b<sub>ij</sub> <br>
     * </p>
     * @param a The left matrix in the multiplication operation. Modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     */
    public static void elementMult( D1Matrix32F a , D1Matrix32F b )
    {
        if( a.numCols != b.numCols || a.numRows != b.numRows ) {
            throw new IllegalArgumentException("The 'a' and 'b' matrices do not have compatible dimensions");
        }

        int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            a.times(i , b.get(i));
        }
    }

    /**
     * <p>Performs the an element by element multiplication operation:<br>
     * <br>
     * c<sub>ij</sub> = a<sub>ij</sub> * b<sub>ij</sub> <br>
     * </p>
     * @param a The left matrix in the multiplication operation. Not modified.
     * @param b The right matrix in the multiplication operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void elementMult( D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        if( a.numCols != b.numCols || a.numRows != b.numRows
                || a.numRows != c.numRows || a.numCols != c.numCols ) {
            throw new IllegalArgumentException("The 'a' and 'b' matrices do not have compatible dimensions");
        }

        int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            c.set( i , a.get(i) * b.get(i) );
        }
    }

    /**
     * <p>Performs the an element by element division operation:<br>
     * <br>
     * a<sub>ij</sub> = a<sub>ij</sub> / b<sub>ij</sub> <br>
     * </p>
     * @param a The left matrix in the division operation. Modified.
     * @param b The right matrix in the division operation. Not modified.
     */
    public static void elementDiv( D1Matrix32F a , D1Matrix32F b )
    {
        if( a.numCols != b.numCols || a.numRows != b.numRows ) {
            throw new IllegalArgumentException("The 'a' and 'b' matrices do not have compatible dimensions");
        }

        int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            a.div(i , b.get(i));
        }
    }

    /**
     * <p>Performs the an element by element division operation:<br>
     * <br>
     * c<sub>ij</sub> = a<sub>ij</sub> / b<sub>ij</sub> <br>
     * </p>
     * @param a The left matrix in the division operation. Not modified.
     * @param b The right matrix in the division operation. Not modified.
     * @param c Where the results of the operation are stored. Modified.
     */
    public static void elementDiv( D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        if( a.numCols != b.numCols || a.numRows != b.numRows
                || a.numRows != c.numRows || a.numCols != c.numCols ) {
            throw new IllegalArgumentException("The 'a' and 'b' matrices do not have compatible dimensions");
        }

        int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            c.set( i , a.get(i) / b.get(i) );
        }
    }

    /**
     * <p>
     * Computes the sum of all the elements in the matrix:<br>
     * <br>
     * sum(i=1:m , j=1:n ; a<sub>ij</sub>)
     * <p>
     *
     * @param mat An m by n matrix. Not modified.
     * @return The sum of the elements.
     */
    public static float elementSum( D1Matrix32F mat ) {
        float total = 0;

        int size = mat.getNumElements();

        for( int i = 0; i < size; i++ ) {
            total += mat.get(i);
        }

        return total;
    }

    /**
     * <p>
     * Computes the sum of the absolute value all the elements in the matrix:<br>
     * <br>
     * sum(i=1:m , j=1:n ; |a<sub>ij</sub>|)
     * <p>
     *
     * @param mat An m by n matrix. Not modified.
     * @return The sum of the absolute value of each element.
     */
    public static float elementSumAbs( D1Matrix32F mat ) {
        float total = 0;

        int size = mat.getNumElements();

        for( int i = 0; i < size; i++ ) {
            total += Math.abs(mat.get(i));
        }

        return total;
    }

    /**
     * <p>
     * Element-wise power operation  <br>
     * c<sub>ij</sub> = a<sub>ij</sub> ^ b<sub>ij</sub>
     * <p>
     *
     * @param A left side
     * @param B right side
     * @param C output (modified)
     */
    public static void elementPower( D1Matrix32F A , D1Matrix32F B , D1Matrix32F C ) {

        if( A.numRows != B.numRows || A.numRows != C.numRows ||
                A.numCols != B.numCols || A.numCols != C.numCols ) {
            throw new IllegalArgumentException("All matrices must be the same shape");
        }

        int size = A.getNumElements();
        for( int i = 0; i < size; i++ ) {
            C.data[i] = (float) Math.pow(A.data[i],B.data[i]);
        }
    }

    /**
     * <p>
     * Element-wise power operation  <br>
     * c<sub>ij</sub> = a ^ b<sub>ij</sub>
     * <p>
     *
     * @param a left scalar
     * @param B right side
     * @param C output (modified)
     */
    public static void elementPower( float a , D1Matrix32F B , D1Matrix32F C ) {

        if( B.numRows != C.numRows || B.numCols != C.numCols ) {
            throw new IllegalArgumentException("All matrices must be the same shape");
        }

        int size = B.getNumElements();
        for( int i = 0; i < size; i++ ) {
            C.data[i] = (float) Math.pow(a,B.data[i]);
        }
    }

    /**
     * <p>
     * Element-wise power operation  <br>
     * c<sub>ij</sub> = a<sub>ij</sub> ^ b
     * <p>
     *
     * @param A left side
     * @param b right scalar
     * @param C output (modified)
     */
    public static void elementPower( D1Matrix32F A , float b, D1Matrix32F C ) {

        if( A.numRows != C.numRows || A.numCols != C.numCols ) {
            throw new IllegalArgumentException("All matrices must be the same shape");
        }

        int size = A.getNumElements();
        for( int i = 0; i < size; i++ ) {
            C.data[i] = (float) Math.pow(A.data[i],b);
        }
    }

    /**
     * <p>
     * Element-wise log operation  <br>
     * c<sub>ij</sub> = Math.log(a<sub>ij</sub>)
     * <p>
     *
     * @param A input
     * @param C output (modified)
     */
    public static void elementLog( D1Matrix32F A , D1Matrix32F C ) {

        if( A.numCols != C.numCols || A.numRows != C.numRows ) {
            throw new IllegalArgumentException("All matrices must be the same shape");
        }

        int size = A.getNumElements();
        for( int i = 0; i < size; i++ ) {
            C.data[i] = (float) Math.log(A.data[i]);
        }
    }

    /**
     * <p>
     * Element-wise exp operation  <br>
     * c<sub>ij</sub> = Math.log(a<sub>ij</sub>)
     * <p>
     *
     * @param A input
     * @param C output (modified)
     */
    public static void elementExp( D1Matrix32F A , D1Matrix32F C ) {

        if( A.numCols != C.numCols || A.numRows != C.numRows ) {
            throw new IllegalArgumentException("All matrices must be the same shape");
        }

        int size = A.getNumElements();
        for( int i = 0; i < size; i++ ) {
            C.data[i] = (float) Math.exp(A.data[i]);
        }
    }

    /**
     * <p>
     * Computes the sum of each row in the input matrix and returns the results in a vector:<br>
     * <br>
     * b<sub>j</sub> = sum(i=1:n ; |a<sub>ji</sub>|)
     * </p>
     *
     * @param input INput matrix whose rows are summed.
     * @param output Optional storage for output.  Must be a vector. If null a row vector is returned. Modified.
     * @return Vector containing the sum of each row in the input.
     */
    public static DenseMatrix32F sumRows( DenseMatrix32F input , DenseMatrix32F output ) {
        if( output == null ) {
            output = new DenseMatrix32F(input.numRows,1);
        } else if( output.getNumElements() != input.numRows )
            throw new IllegalArgumentException("Output does not have enough elements to store the results");

        for( int row = 0; row < input.numRows; row++ ) {
            float total = 0;

            int end = (row+1)*input.numCols;
            for( int index = row*input.numCols; index < end; index++ ) {
                total += input.data[index];
            }

            output.set(row,total);
        }
        return output;
    }

    /**
     * <p>
     * Computes the sum of each column in the input matrix and returns the results in a vector:<br>
     * <br>
     * b<sub>j</sub> = sum(i=1:m ; |a<sub>ij</sub>|)
     * </p>
     *
     * @param input INput matrix whose rows are summed.
     * @param output Optional storage for output.  Must be a vector. If null a column vector is returned. Modified.
     * @return Vector containing the sum of each row in the input.
     */
    public static DenseMatrix32F sumCols( DenseMatrix32F input , DenseMatrix32F output ) {
        if( output == null ) {
            output = new DenseMatrix32F(1,input.numCols);
        } else if( output.getNumElements() != input.numCols )
            throw new IllegalArgumentException("Output does not have enough elements to store the results");

        for( int cols = 0; cols < input.numCols; cols++ ) {
            float total = 0;

            int index = cols;
            int end = index + input.numCols*input.numRows;
            for( ; index < end; index += input.numCols ) {
                total += input.data[index];
            }

            output.set(cols,total);
        }
        return output;
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * a = a + b <br>
     * a<sub>ij</sub> = a<sub>ij</sub> + b<sub>ij</sub> <br>
     * </p>
     *
     * @param a A Matrix. Modified.
     * @param b A Matrix. Not modified.
     */
    public static void addEquals( D1Matrix32F a , D1Matrix32F b )
    {
        if( a.numCols != b.numCols || a.numRows != b.numRows ) {
            throw new IllegalArgumentException("The 'a' and 'b' matrices do not have compatible dimensions");
        }

        final int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            a.plus(i, b.get(i));
        }
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * a = a +  &beta; * b  <br>
     * a<sub>ij</sub> = a<sub>ij</sub> + &beta; * b<sub>ij</sub>
     * </p>
     *
     * @param beta The number that matrix 'b' is multiplied by.
     * @param a A Matrix. Modified.
     * @param b A Matrix. Not modified.
     */
    public static void addEquals( D1Matrix32F a , float beta, D1Matrix32F b )
    {
        if( a.numCols != b.numCols || a.numRows != b.numRows ) {
            throw new IllegalArgumentException("The 'a' and 'b' matrices do not have compatible dimensions");
        }

        final int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            a.plus(i, beta * b.get(i));
        }
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = a + b <br>
     * c<sub>ij</sub> = a<sub>ij</sub> + b<sub>ij</sub> <br>
     * </p>
     *
     * <p>
     * Matrix C can be the same instance as Matrix A and/or B.
     * </p>
     *
     * @param a A Matrix. Not modified.
     * @param b A Matrix. Not modified.
     * @param c A Matrix where the results are stored. Modified.
     */
    public static void add( final D1Matrix32F a , final D1Matrix32F b , final D1Matrix32F c )
    {
        if( a.numCols != b.numCols || a.numRows != b.numRows
                || a.numCols != c.numCols || a.numRows != c.numRows ) {
            throw new IllegalArgumentException("The matrices are not all the same dimension.");
        }

        final int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            c.set( i , a.get(i)+b.get(i) );
        }
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = a + &beta; * b <br>
     * c<sub>ij</sub> = a<sub>ij</sub> + &beta; * b<sub>ij</sub> <br>
     * </p>
     *
     * <p>
     * Matrix C can be the same instance as Matrix A and/or B.
     * </p>
     *
     * @param a A Matrix. Not modified.
     * @param beta Scaling factor for matrix b.
     * @param b A Matrix. Not modified.
     * @param c A Matrix where the results are stored. Modified.
     */
    public static void add( D1Matrix32F a , float beta , D1Matrix32F b , D1Matrix32F c )
    {
        if( a.numCols != b.numCols || a.numRows != b.numRows
                || a.numCols != c.numCols || a.numRows != c.numRows ) {
            throw new IllegalArgumentException("The matrices are not all the same dimension.");
        }

        final int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            c.set( i , a.get(i)+beta*b.get(i) );
        }
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = &alpha; * a + &beta; * b <br>
     * c<sub>ij</sub> = &alpha; * a<sub>ij</sub> + &beta; * b<sub>ij</sub> <br>
     * </p>
     *
     * <p>
     * Matrix C can be the same instance as Matrix A and/or B.
     * </p>
     *
     * @param alpha A scaling factor for matrix a.
     * @param a A Matrix. Not modified.
     * @param beta A scaling factor for matrix b.
     * @param b A Matrix. Not modified.
     * @param c A Matrix where the results are stored. Modified.
     */
    public static void add( float alpha , D1Matrix32F a , float beta , D1Matrix32F b , D1Matrix32F c )
    {
        if( a.numCols != b.numCols || a.numRows != b.numRows
                || a.numCols != c.numCols || a.numRows != c.numRows ) {
            throw new IllegalArgumentException("The matrices are not all the same dimension.");
        }

        final int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            c.set(i , alpha*a.get(i) + beta*b.get(i));
        }
    }

    /**
     * <p>Performs the following operation:<br>
     * <br>
     * c = &alpha; * a + b <br>
     * c<sub>ij</sub> = &alpha; * a<sub>ij</sub> + b<sub>ij</sub> <br>
     * </p>
     *
     * <p>
     * Matrix C can be the same instance as Matrix A and/or B.
     * </p>
     *
     * @param alpha A scaling factor for matrix a.
     * @param a A Matrix. Not modified.
     * @param b A Matrix. Not modified.
     * @param c A Matrix where the results are stored. Modified.
     */
    public static void add( float alpha , D1Matrix32F a , D1Matrix32F b , D1Matrix32F c )
    {
        if( a.numCols != b.numCols || a.numRows != b.numRows
                || a.numCols != c.numCols || a.numRows != c.numRows ) {
            throw new IllegalArgumentException("The matrices are not all the same dimension.");
        }

        final int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            c.set( i , alpha*a.get(i) + b.get(i));
        }
    }

    /**
     * <p>Performs an in-place scalar addition:<br>
     * <br>
     * a = a + val<br>
     * a<sub>ij</sub> = a<sub>ij</sub> + val<br>
     * </p>
     *
     * @param a A matrix.  Modified.
     * @param val The value that's added to each element.
     */
    public static void add( D1Matrix32F a , float val ) {
        final int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            a.plus( i , val);
        }
    }

    /**
     * <p>Performs scalar addition:<br>
     * <br>
     * c = a + val<br>
     * c<sub>ij</sub> = a<sub>ij</sub> + val<br>
     * </p>
     *
     * @param a A matrix. Not modified.
     * @param c A matrix. Modified.
     * @param val The value that's added to each element.
     */
    public static void add( D1Matrix32F a , float val , D1Matrix32F c ) {
        if( a.numRows != c.numRows || a.numCols != c.numCols ) {
            throw new IllegalArgumentException("Dimensions of a and c do not match.");
        }

        final int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            c.data[i] = a.data[i] + val;
        }
    }

    /**
     * <p>Performs matrix scalar subtraction:<br>
     * <br>
     * c = a - val<br>
     * c<sub>ij</sub> = a<sub>ij</sub> - val<br>
     * </p>
     *
     * @param a (input) A matrix. Not modified.
     * @param val (input) The value that's subtracted to each element.
     * @param c (Output) A matrix. Modified.
     */
    public static void subtract( D1Matrix32F a , float val , D1Matrix32F c ) {
        if( a.numRows != c.numRows || a.numCols != c.numCols ) {
            throw new IllegalArgumentException("Dimensions of a and c do not match.");
        }

        final int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            c.data[i] = a.data[i] - val;
        }
    }

    /**
     * <p>Performs matrix scalar subtraction:<br>
     * <br>
     * c = val - a<br>
     * c<sub>ij</sub> = val - a<sub>ij</sub><br>
     * </p>
     *
     * @param val (input) The value that's subtracted to each element.
     * @param a (input) A matrix. Not modified.
     * @param c (Output) A matrix. Modified.
     */
    public static void subtract( float val , D1Matrix32F a , D1Matrix32F c ) {
        if( a.numRows != c.numRows || a.numCols != c.numCols ) {
            throw new IllegalArgumentException("Dimensions of a and c do not match.");
        }

        final int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            c.data[i] = val - a.data[i];
        }
    }

    /**
     * <p>Performs the following subtraction operation:<br>
     * <br>
     * a = a - b  <br>
     * a<sub>ij</sub> = a<sub>ij</sub> - b<sub>ij</sub>
     * </p>
     *
     * @param a A Matrix. Modified.
     * @param b A Matrix. Not modified.
     */
    public static void subtractEquals(D1Matrix32F a, D1Matrix32F b)
    {
        if( a.numCols != b.numCols || a.numRows != b.numRows ) {
            throw new IllegalArgumentException("The 'a' and 'b' matrices do not have compatible dimensions");
        }

        final int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            a.data[i] -= b.data[i];
        }
    }

    /**
     * <p>Performs the following subtraction operation:<br>
     * <br>
     * c = a - b  <br>
     * c<sub>ij</sub> = a<sub>ij</sub> - b<sub>ij</sub>
     * </p>
     * <p>
     * Matrix C can be the same instance as Matrix A and/or B.
     * </p>
     *
     * @param a A Matrix. Not modified.
     * @param b A Matrix. Not modified.
     * @param c A Matrix. Modified.
     */
    public static void subtract(D1Matrix32F a, D1Matrix32F b, D1Matrix32F c)
    {
        if( a.numCols != b.numCols || a.numRows != b.numRows ) {
            throw new IllegalArgumentException("The 'a' and 'b' matrices do not have compatible dimensions");
        }

        final int length = a.getNumElements();

        for( int i = 0; i < length; i++ ) {
            c.data[i] = a.data[i] - b.data[i];
        }
    }

    /**
     * <p>
     * Performs an in-place element by element scalar multiplication.<br>
     * <br>
     * a<sub>ij</sub> = &alpha;*a<sub>ij</sub>
     * </p>
     *
     * @param a The matrix that is to be scaled.  Modified.
     * @param alpha the amount each element is multiplied by.
     */
    public static void scale( float alpha , D1Matrix32F a )
    {
        // on very small matrices (2 by 2) the call to getNumElements() can slow it down
        // slightly compared to other libraries since it involves an extra multiplication.
        final int size = a.getNumElements();

        for( int i = 0; i < size; i++ ) {
            a.data[i] *= alpha;
        }
    }

    /**
     * <p>
     * Performs an element by element scalar multiplication.<br>
     * <br>
     * b<sub>ij</sub> = &alpha;*a<sub>ij</sub>
     * </p>
     *
     * @param alpha the amount each element is multiplied by.
     * @param a The matrix that is to be scaled.  Not modified.
     * @param b Where the scaled matrix is stored. Modified.
     */
    public static void scale( float alpha , D1Matrix32F a , D1Matrix32F b)
    {
        if( a.numRows != b.numRows || a.numCols != b.numCols )
            throw new IllegalArgumentException("Matrices must have the same shape");

        final int size = a.getNumElements();

        for( int i = 0; i < size; i++ ) {
            b.data[i] = a.data[i]*alpha;
        }
    }

    /**
     * <p>
     * Performs an in-place element by element scalar division with the scalar on top.<br>
     * <br>
     * a<sub>ij</sub> = &alpha/a<sub>ij</sub>;
     * </p>
     *
     * @param a The matrix whose elements are divide the scalar.  Modified.
     * @param alpha top value in division
     */
    public static void divide( float alpha , D1Matrix32F a )
    {
        final int size = a.getNumElements();

        for( int i = 0; i < size; i++ ) {
            a.data[i] = alpha/a.data[i];
        }
    }

    /**
     * <p>
     * Performs an in-place element by element scalar division with the scalar on bottom.<br>
     * <br>
     * a<sub>ij</sub> = a<sub>ij</sub>/&alpha;
     * </p>
     *
     * @param a The matrix whose elements are to be divided.  Modified.
     * @param alpha the amount each element is divided by.
     */
    public static void divide( D1Matrix32F a , float alpha)
    {
        final int size = a.getNumElements();

        for( int i = 0; i < size; i++ ) {
            a.data[i] /= alpha;
        }
    }

    /**
     * <p>
     * Performs an element by element scalar division with the scalar on top.<br>
     * <br>
     * b<sub>ij</sub> = &alpha/a<sub>ij</sub>;
     * </p>
     *
     * @param alpha The numerator.
     * @param a The matrix whose elements are the divisor.  Not modified.
     * @param b Where the results are stored. Modified.
     */
    public static void divide( float alpha , D1Matrix32F a , D1Matrix32F b)
    {
        if( a.numRows != b.numRows || a.numCols != b.numCols )
            throw new IllegalArgumentException("Matrices must have the same shape");

        final int size = a.getNumElements();

        for( int i = 0; i < size; i++ ) {
            b.data[i] = alpha/a.data[i];
        }
    }

    /**
     * <p>
     * Performs an element by element scalar division with the scalar on botton.<br>
     * <br>
     * b<sub>ij</sub> = a<sub>ij</sub> /&alpha;
     * </p>
     *
     * @param a The matrix whose elements are to be divided.  Not modified.
     * @param alpha the amount each element is divided by.
     * @param b Where the results are stored. Modified.
     */
    public static void divide( D1Matrix32F a , float alpha  , D1Matrix32F b)
    {
        if( a.numRows != b.numRows || a.numCols != b.numCols )
            throw new IllegalArgumentException("Matrices must have the same shape");

        final int size = a.getNumElements();

        for( int i = 0; i < size; i++ ) {
            b.data[i] = a.data[i]/alpha;
        }
    }

    /**
     * <p>
     * Changes the sign of every element in the matrix.<br>
     * <br>
     * a<sub>ij</sub> = -a<sub>ij</sub>
     * </p>
     *
     * @param a A matrix. Modified.
     */
    public static void changeSign( D1Matrix32F a )
    {
        final int size = a.getNumElements();

        for( int i = 0; i < size; i++ ) {
            a.data[i] = -a.data[i];
        }
    }

    /**
     * <p>
     * Changes the sign of every element in the matrix.<br>
     * <br>
     * output<sub>ij</sub> = -input<sub>ij</sub>
     * </p>
     *
     * @param input A matrix. Modified.
     */
    public static void changeSign( D1Matrix32F input , D1Matrix32F output)
    {
        if( input.numRows != output.numRows || input.numCols != output.numCols )
            throw new IllegalArgumentException("Matrices must have the same shape");

        final int size = input.getNumElements();

        for( int i = 0; i < size; i++ ) {
            output.data[i] = -input.data[i];
        }
    }

    /**
     * <p>
     * Sets every element in the matrix to the specified value.<br>
     * <br>
     * a<sub>ij</sub> = value
     * <p>
     *
     * @param a A matrix whose elements are about to be set. Modified.
     * @param value The value each element will have.
     */
    public static void fill(D1Matrix32F a, float value)
    {
        Arrays.fill(a.data,0,a.getNumElements(),value);
    }

    /**
     * <p>
     * Puts the augmented system matrix into reduced row echelon form (RREF) using Gauss-Jordan
     * elimination with row (partial) pivots.  A matrix is said to be in RREF is the following conditions are true:
     * </p>
     *
     * <ol>
     *     <li>If a row has non-zero entries, then the first non-zero entry is 1.  This is known as the leading one.</li>
     *     <li>If a column contains a leading one then all other entries in that column are zero.</li>
     *     <li>If a row contains a leading 1, then each row above contains a leading 1 further to the left.</li>
     * </ol>
     *
     * <p>
     * [1] Page 19 in, Otter Bretscherm "Linear Algebra with Applications" Prentice-Hall Inc, 1997
     * </p>
     *
     * @see RrefGaussJordanRowPivot
     *
     * @param A Input matrix.  Unmodified.
     * @param numUnknowns Number of unknowns/columns that are reduced. Set to -1 to default to
     *                       Math.min(A.numRows,A.numCols), which works for most systems.
     * @param reduced Storage for reduced echelon matrix. If null then a new matrix is returned. Modified.
     * @return Reduced echelon form of A
     */
    public static DenseMatrix32F rref( DenseMatrix32F A , int numUnknowns, DenseMatrix32F reduced ) {
//        if( reduced == null ) {
//            reduced = new DenseMatrix32F(A.numRows,A.numCols);
//        } else if( reduced.numCols != A.numCols || reduced.numRows != A.numRows )
//            throw new IllegalArgumentException("'re' must have the same shape as the original input matrix");
//
//        if( numUnknowns <= 0 )
//            numUnknowns = Math.min(A.numCols,A.numRows);
//
//        ReducedRowEchelonForm<DenseMatrix32F> alg = new RrefGaussJordanRowPivot();
//        alg.setTolerance(elementMaxAbs(A)* UtilEjml.EPS*Math.max(A.numRows,A.numCols));
//
//        reduced.set(A);
//        alg.reduce(reduced, numUnknowns);

        return reduced;
    }
}
