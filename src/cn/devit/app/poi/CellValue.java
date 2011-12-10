package cn.devit.app.poi;

class CellValue{

	//store perpend text
	StringBuilder sb0 = new StringBuilder();
	//store append text;
	StringBuilder sb1 = new StringBuilder();
	//store no wrap text;
	String text = "";

	public CellValue() {
		super();
	}
	public void setValue(String value){
		sb0 = new StringBuilder();
		sb1 = new StringBuilder();
		text = value;
	}

	public String getNoWrapText(){
		return text;
	}

	public void setNoWrapText(String value){
		text =value;
	}

	public void wrap(String perpend,String append){
		perpend(perpend);
		append(append);
	}

	public void append(String string){
		sb1.append(string);
	}

	public void perpend(String string){
		sb0.insert(0, string);
	}

	public String toString(){
		return sb0.toString()+text+sb1.toString();
	}

}