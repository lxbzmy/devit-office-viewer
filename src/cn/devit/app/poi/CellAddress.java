package cn.devit.app.poi;

import org.apache.poi.ss.usermodel.Cell;

public class CellAddress {

	int col;
	int row;
	/**
	 * @param col column index of cell,0 base
	 * @param row column index of row,0 base
	 */
	public CellAddress(int col,int row) {
		this.col = col;
		this.row = row;
	}


	/**
	 * @param cell cell
	 * @see Cell
	 */
	public CellAddress(Cell cell){
		this.col = cell.getColumnIndex();
		this.row = cell.getRowIndex();
	}

	/**
	 * excel max column is IV
	 * 65-90 A-Z
	 */
	public String toString(){

		int c1 = this.col+1;

		int r1 = this.row+1;

		 int p1 = c1%26;
		 int p2 = c1/26;
		 char char1 = (char)(p1+64);
		 char char2 = (char)(p2+64);
		 if(p2>0){
			 return String.valueOf(char2)+String.valueOf(char1)+r1;
		 }else{
			 return String.valueOf(char1)+r1;
		 }
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.col;
		result = prime * result + this.row;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CellAddress other = (CellAddress) obj;
		if (this.col != other.col)
			return false;
		if (this.row != other.row)
			return false;
		return true;
	}
}
