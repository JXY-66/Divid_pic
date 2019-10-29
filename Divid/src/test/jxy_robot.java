package test;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

class Matlab2Java {
	
	public Matrix point_multi(Matrix A, Matrix B) {
		int col = A.getColumnDimension();
		int row = A.getRowDimension();
		double[][] C = new double[row][col];
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				C[i][j] = A.get(i, j) * B.get(i, j);
			}
		}
		return new Matrix(C);
	}
	
	public Matrix alter_angle_after(double[] rad, Matrix X) {
		if(X.getRowDimension()==1) {
			X = X.transpose();
		}
		double rx = rad[0]; double ry = rad[1]; double rz = rad[2];
		double[][] A = new double[3][3];
		A[0][0] = Math.cos(rz)*Math.cos(ry);
		A[0][1] = Math.cos(rz)*Math.sin(ry)*Math.sin(rx)-Math.sin(rz)*Math.cos(rx);
		A[0][2] = Math.cos(rz)*Math.sin(ry)*Math.cos(rx)+Math.sin(rz)*Math.sin(rx);
		A[1][0] = Math.sin(rz)*Math.cos(ry);
		A[1][1] = Math.sin(rz)*Math.sin(ry)*Math.sin(rx)+Math.cos(rz)*Math.cos(rx);
		A[1][2] = Math.sin(rz)*Math.sin(ry)*Math.cos(rx)-Math.cos(rz)*Math.sin(rx);
		A[2][0] = -1*Math.sin(ry);
		A[2][1] = Math.cos(ry)*Math.sin(rx);
		A[2][2] = Math.cos(ry)*Math.cos(rx);
		Matrix A1 = new Matrix(A);
		return A1.times(X);
	}
	
	public Matrix alter_angle_orginal(double[] rad, Matrix X) {
		if(X.getRowDimension()==1) {
			X = X.transpose();
		}
		double rx = rad[0]; double ry = rad[1]; double rz = rad[2];
		double[][] Rx = new double[3][3];
		double[][] Ry = new double[3][3];
		double[][] Rz = new double[3][3];
		Rx[0][0] = 1; Rx[0][1] = 0; Rx[0][2] = 0;
		Rx[1][0] = 0; Rx[1][1] = Math.cos(rx); Rx[1][2] = -1*Math.sin(rx);
		Rx[2][0] = 0; Rx[2][1] = Math.sin(rx); Rx[2][2] = Math.cos(rx);
		
		Ry[0][0] = Math.cos(ry); Ry[0][1] = 0; Ry[0][2] = Math.sin(ry);
		Ry[1][0] = 0; Ry[1][1] = 1; Ry[1][2] = 0;
		Ry[2][0] = -1*Math.sin(ry); Ry[2][1] = 0; Ry[2][2] = Math.cos(ry);
		
		Rx[0][0] = Math.cos(rz); Rx[0][1] = -1*Math.sin(rz); Rx[0][2] = 0;
		Rx[1][0] = Math.sin(rz); Rx[1][1] = Math.cos(rz); Rx[1][2] = 0;
		Rx[2][0] = 0; Rx[2][1] = 0; Rx[2][2] = 1;
		
		Matrix Rx1 = new Matrix(Rx);
		Matrix Ry1 = new Matrix(Ry);
		Matrix Rz1 = new Matrix(Rz);
		
		return Rx1.times(Ry1).times(Rz1).times(X);
	}
	
	public double[] ang_rad(double[] ang) {
		double rx = Math.PI*ang[0]/180;
		double ry = Math.PI*ang[1]/180;
		double rz = Math.PI*ang[2]/180;
		double ans[] = new double[3];
		ans[0]=rx; ans[1]=ry; ans[2]=rz;
		return ans;
	}
	
	public double[] rad_ang(double[] rad) {
		double rx = 180*rad[0]/Math.PI;
		double ry = 180*rad[1]/Math.PI;
		double rz = 180*rad[2]/Math.PI;
		double[] ans = new double[3];
		ans[0]=rx; ans[1]=ry; ans[2]=rz;
		return ans;
	}
	
	public double[][] matrix_oula(Matrix A) {
		double[][] D = new double[1][6];
		D[0][0] = A.get(0, 3); D[0][1] = A.get(1, 3); D[0][2] = A.get(2, 3);
		double rx = Math.atan2(A.get(2, 1), A.get(2, 2));
		double ry = Math.atan2(-A.get(2, 0), Math.sqrt(A.get(0, 0)*A.get(0, 0)+A.get(1, 0)*A.get(1, 0)));
		double rz = Math.atan2(A.get(1, 0), A.get(0, 0));
		D[0][3] = rx; D[0][4] = ry; D[0][5] = rz;
		return D;
	}
	
	public static void print(Matrix A) {
		int a_col = A.getColumnDimension();
		int a_row = A.getRowDimension();
		for(int i = 0; i < a_row; i++) {
			for(int j = 0; j < a_col; j++) {
				System.out.print(A.get(i, j) + " ");
			}
			System.out.println();
		}
	}
	
	public Matrix point_matching(Matrix A, Matrix B) {
		int a_col = A.getColumnDimension();
		int a_row = A.getRowDimension();
		int b_col = B.getColumnDimension();
		int b_row = B.getRowDimension();
		double[][] centroid_A = new double[1][a_col];
		double[][] centroid_B = new double[1][b_col];
		double[][] centroid_A1 = new double[a_row][a_col];
		double[][] centroid_B1 = new double[b_row][b_col];
		
		
		double tmp = 0;
		for(int j = 0; j < a_col; j++) {
			for(int i = 0; i < a_row; i++) {
				tmp += A.get(i, j);
			}
			tmp = tmp / a_row;
			centroid_A[0][j] = tmp;
			for(int i = 0; i < a_row; i++) {
				centroid_A1[i][j] = tmp;
			}
			tmp = 0;
		}

		for(int j = 0; j < b_col; j++) {
			for(int i = 0; i < b_row; i++) {
				tmp += B.get(i, j);
			}
			tmp = tmp / b_row;
			centroid_B[0][j] = tmp;
			for(int i = 0; i < b_row; i++) {
				centroid_B1[i][j] = tmp;
			}
			tmp = 0;
		}
		Matrix cen_A_matrix = new Matrix(centroid_A);
		print(cen_A_matrix);
		Matrix cen_B_matrix = new Matrix(centroid_B);
		print(cen_B_matrix);
		Matrix cen_A1_matrix = new Matrix(centroid_A1);//cen_A1_matrix对应repmat(centroid_A, N, 1)
		print(cen_A1_matrix);
		Matrix cen_B1_matrix = new Matrix(centroid_B1);//cen_B1_matrix对应repmat(centroid_B, N, 1)
		print(cen_B1_matrix);
		
		
		Matrix A_move = (A.plus(cen_A1_matrix.times(-1)));
		print(A_move);
		Matrix B_move = (B.plus(cen_B1_matrix.times(-1)));
		print(B_move);
		Matrix C_move = A_move.transpose();
		print(C_move);
		Matrix H = C_move.times(B_move);
		print(H);
		
		double[] A_norm_tmp = new double[a_row];
		double[] B_norm_tmp = new double[b_row];
		Matrix t1 = point_multi(A_move, A_move);
		print(t1);
		Matrix t2 = point_multi(B_move, B_move);
		print(t2);
		int row_a = t1.getRowDimension();
		int col_a = t1.getColumnDimension();
		int row_b = t2.getRowDimension();
		int col_b = t2.getColumnDimension();
		for(int i = 0; i < row_a; i++) {
			double tmp1 = 0;
			for(int j = 0; j < col_a; j++) {
				tmp1 += t1.get(i, j);
			}
			A_norm_tmp[i] = tmp1;
		}
		for(int i = 0; i < row_b; i++) {
			double tmp1 = 0;
			for(int j = 0; j < col_b; j++) {
				tmp1 += t2.get(i, j);
			}
			B_norm_tmp[i] = tmp1;
		}
		double lam2 = 0;
		for(int i = 0; i < row_a; i++) {
			lam2 += A_norm_tmp[i] / B_norm_tmp[i];
		}
		lam2 = lam2/row_a;
		lam2 = 1/Math.sqrt(lam2);
		
		SingularValueDecomposition s = H.svd();
		Matrix U = s.getU();
		print(U);
		Matrix S = s.getS();
		print(S);
		Matrix V = s.getV();
		print(V);
		Matrix R = V.times(U.transpose());
		print(R);
		if(R.det() < 0) {
			for(int i = 0; i < V.getRowDimension(); i++) {
				V.set(i, 2, -1*V.get(i, 2));
			}
			R = V.times(U.transpose());
		}
		Matrix t = R.times(-1*lam2).times(cen_A_matrix.transpose()).plus(cen_B_matrix.transpose());
		print(t);
		R = R.times(lam2);
		double[][] ans = new double[R.getRowDimension()+1][R.getColumnDimension()+1];
		for(int i = 0; i < R.getRowDimension(); i++) {
			for(int j = 0; j < R.getColumnDimension(); j++) {
				ans[i][j] = R.get(i, j);
			}
		}
		for(int i = 0; i < t.getRowDimension(); i++) {
			ans[i][R.getColumnDimension()] = t.get(i, 0);
		}
		ans[R.getRowDimension()][R.getColumnDimension()] = 1;
		return new Matrix(ans);
	}

	public Matrix rigidbody_point(double[] rigid_ori, double[] rigid_new, Matrix X) {
		if(X.getRowDimension()==1) {
			X = X.transpose();
		}
		double[] ori_cor = new double[3];
		double[] ori_ang = new double[3];
		double[] new_cor = new double[3];
		double[] new_ang = new double[3];
		ori_cor[0]=rigid_ori[0];ori_cor[1]=rigid_ori[1];ori_cor[2]=rigid_ori[2];
		ori_ang[0]=rigid_ori[3];ori_ang[1]=rigid_ori[4];ori_ang[2]=rigid_ori[5];
		new_cor[0]=rigid_new[0];new_cor[1]=rigid_new[1];new_cor[2]=rigid_new[2];
		new_ang[0]=rigid_new[3];new_ang[1]=rigid_new[4];new_ang[2]=rigid_new[5];
		double[] ang = new double[3];
		ang[0]=new_ang[0]-ori_ang[0];
		ang[1]=new_ang[1]-ori_ang[1];
		ang[2]=new_ang[2]-ori_ang[2];
		
		double[][] x = new double[X.getRowDimension()][1];
		for(int i = 0; i < X.getRowDimension(); i++) {
			x[i][0] = X.get(i, 0)-ori_cor[i];
		}
		Matrix x1 = new Matrix(x);
		double[] rad = new double[3];
		rad = ang_rad(ang);
		Matrix y = alter_angle_after(rad, x1);
		double[][] Y = new double[y.getRowDimension()][1];
		for(int i = 0; i < y.getRowDimension(); i++) {
			Y[i][0] = y.get(i, 0)+new_cor[i];
		}
		return new Matrix(Y);
	}
	
	public double[] robot_state(Matrix focus, Matrix point, Matrix M) {
		if(focus.getRowDimension() == 1) {
			focus = focus.transpose();
		}
		if(point.getRowDimension() == 1) {
			point = point.transpose();
		}
		if(M.getRowDimension() == 1) {
			M = M.transpose();
		}
		double[] focus_n_cor = new double[3];
		double[] point_n_cor = new double[3];
		focus_n_cor[0] = 1000*(focus.get(2, 0)+M.get(2, 0));
		focus_n_cor[1] = 1000*(focus.get(0, 0)+M.get(0, 0));
		focus_n_cor[2] = 1000*(focus.get(1, 0)+M.get(1, 0));
		point_n_cor[0] = 1000*(point.get(2, 0)+M.get(2, 0));
		point_n_cor[1] = 1000*(point.get(0, 0)+M.get(0, 0));
		point_n_cor[2] = 1000*(point.get(1, 0)+M.get(1, 0));;
		double[] vec = new double[3];
		vec[0] = point_n_cor[0]-focus_n_cor[0];
		vec[1] = point_n_cor[1]-focus_n_cor[1];
		vec[2] = point_n_cor[2]-focus_n_cor[2];
		double a = vec[0]; double b = vec[1]; double c = vec[2];
		double RX = Math.atan(b/c);
		double RY = Math.acos(Math.sqrt(b*b+c*c)/Math.sqrt(a*a+b*b+c*c));
		if(a == 0) {
			RY = 0;
		}
		else if(a > 0) {
			RY = -RY;
		}
		double RZ = 0;
		double[] rad = new double[3];
		rad[0]=RX; rad[1]=RY; rad[2]=RZ;
		double[] ang = new double[3];
		ang = rad_ang(rad);
		double[] point_n = new double[6];
		point_n[0] = point_n_cor[0]; point_n[1] = point_n_cor[1]; point_n[2] = point_n_cor[2];
		point_n[3] = ang[0]; point_n[4] = ang[1]; point_n[5] = ang[2];
		return point_n;
	}
	
	public double[] siyuan_oula(double[] A) {
		double a = A[0]; double b = A[1]; double c = A[2]; double d = A[3];
		double rx = Math.atan2(2*(a*b+c*d),(1-2*(b*b+c*c)));
		double ry = Math.asin(2*(a*c-b*d));
		double rz = Math.atan2(2*(a*d+b*c),(1-2*(c*c+d*d)));
		rx=rx*180/Math.PI;ry=ry*180/Math.PI;rz=rz*180/Math.PI;
		double cc = rx;
		double aa = 180-rz;
		double bb = -ry;
		double[] ang = new double[3];
		ang[0] = aa; ang[1] = bb; ang[2] = cc;
		double[] rad = new double[3];
		rad = ang_rad(ang);
		return rad;
	}
	
	public void robot_data() {
		
	}
	
	public void urlrigidbody() {
		
	}
	
	public void urlrigidpoint() {
		
	}
	
	public static void main(String[] args){
		double[][] c = new double[1][4];
		c[0][0]=20.9893654; c[0][1]=-45.7536193; c[0][2]=60.4174116; c[0][3] = 1000;
		Matrix c1 = new Matrix(c);
		
		double[][] cc = new double[1][4];
		cc[0][0]=16.921; cc[0][1]=-30.513; cc[0][2]=45.044; cc[0][3] = 1000;
		Matrix cc1 = new Matrix(cc);
		
		double[][] A = new double[4][3];
		A[0][0]=-26.168; A[0][1]=-173.186; A[0][2]=-85.442;
		A[1][0]=57.947; A[1][1]=-151.228; A[1][2]=-18.373;
		A[2][0]=-71.928; A[2][1]=-152.71; A[2][2]=-13.065;
		A[3][0]=-10.696; A[3][1]=-141.411; A[3][2]=-9.544;
		Matrix A1 = new Matrix(A);
		
		double[][] M = new double[3][1];
		M[0][0]=0.01512; M[1][0]=-0.9887; M[2][0]=-1.4482;
		Matrix M1 = new Matrix(M);
		
		double[][] rigid_ori = new double[6][1];
		rigid_ori[0][0]=0.204781; rigid_ori[1][0]=1.02233; rigid_ori[2][0]=-0.0415102;
		rigid_ori[3][0]=0; rigid_ori[4][0]=0; rigid_ori[5][0]=0;
		Matrix rigid_ori1 = new Matrix(rigid_ori);
		
		double[][] B = new double[4][3];
		B[0][0]=0.1760424; B[0][1]=0.9763484; B[0][2]=-0.0596537;
		B[1][0]=0.204781; B[1][1]=1.02233; B[1][2]=-0.0415102;
		B[2][0]=0.21401963; B[2][1]=1.0436104; B[2][2]=-0.0977689;
		B[3][0]=0.2220812; B[3][1]=1.0402419; B[3][2]=-0.03716458;
		Matrix B1 = new Matrix(B);
		
		c1 = c1.times(0.001);
		cc1 = cc1.times(0.001);
		A1 = A1.times(0.001);
		
		Matlab2Java m2j = new Matlab2Java();
		Matrix ll = m2j.point_matching(A1, B1);
		print(ll);
		print(cc1);
		Matrix focus_camera = ll.times(cc1.transpose());
		print(focus_camera);
		print(c1);
		Matrix point_camera = ll.times(c1.transpose());
		print(point_camera);
		
		System.out.println("ok");
	}
}