//ユーザの情報を伴ったコンテンツのJSONを返す。
package test;	//テスト用パッケージ。

//入力用クラスをインポートする。
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
//SQL関係のクラスをインポートする
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

//サーブレットでのエラー用の例外クラスをインポートする。
import javax.servlet.ServletException;
//サーブレットのクラス群をインポートする。
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//サーブレットのOrderbillクラスを宣言する。
public class Orderbill extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4951720880589062491L;
	/** 日付フォーマット */
	static public final String DATE_PATTERN ="yyyy/MM/dd";
	/*
	 * 関数名:String getResult()
	 * 概要  :レコードを取得する。
	 * 引数  :String Sql:実行するクエリ
	 * 返却値  :String:JSONの文字列。
	 * 作成者:T.Masuda
	 * 作成日:2015.05.11
	 */
	 String getResult(String sql) throws SQLException {
		//文字列を返却するために格納する変数 reStrを宣言、初期化する。
		String retStr = "";
		//クエリを引数にgetResultSetメソッドをコールし、結果セットを取得する。
		ResultSet rs = DBManager.getResultSet(sql);
		
		//結果セットからJSON文字列を作成する。
		retStr = getResultJSONString(rs);
		
		//retStrを返す。
		return retStr;
	}

	/*
	 * 関数名:String  getResultJSONString(ResultSet rs)
	 * 概要  :必要となるJSONの文字列作成して返すサブ関数。
	 * 引数  :ResultSet rs:DBから返ってきた結果セット。
	 * 返却値  :String:JSONの文字列。
	 * 作成者:T.Masuda
	 * 作成日:2015.05.11
	 */
	String  getResultJSONString(ResultSet rs) throws SQLException {
		//文字列を作成するためのクラスのインスタンスを用意する。
		StringBuilder sb = new StringBuilder();
		//日付型を変換するクラスのインスタンスを用意する。
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
		//結果セットのメタデータを取得する。
		ResultSetMetaData rsm = rs.getMetaData();
		//列数を取得する。
		int ccount = rsm.getColumnCount();
		//JSON配列の開始の括弧をsbにセットする。
		sb.append("[");
		String sColVal = "";
		//結果セットのポインタを進めながらループする。
		while(rs.next()){
			sb.append("{");	//sbにオブジェクトの開始の括弧を追加する。
			//rsの列を走査する
			for(int iLoop = 0 ;iLoop < ccount ; iLoop ++){
				//列名を取得する
				String sColName = rsm.getColumnLabel(iLoop + 1);

				//キーを追加する。
				sb.append("\""+ sColName +"\":\"");
				
				//列の型を取得する
				String colType = rsm.getColumnTypeName(iLoop + 1);
				//列の型がdateである
				if(colType.equals("DATE")){
					//値を取得する
					sColVal = formatter.format(rs.getDate(sColName));
				//列の型がintである
				} else if(colType.equals("INT")){
					//値を取得する
					sColVal = String.valueOf(rs.getInt(sColName));
				//列の型が文字列である
				} else {
					//値を取得する
					sColVal = rs.getString(sColName);
				}
				//ブログタイトル部分のJSON文字列を作成する。
				sb.append(sColVal+"\",");
			}
			
			sb.setLength(sb.length() - 1);	//区切りの点を除去する
			sb.append("},");				//オブジェクトの括弧を閉じる
		}
		
		sb.setLength(sb.length() - 1);	//区切りの点を除去する
		sb.append("]");					//配列の括弧を閉じる
		
		// 作成した文字列を返す。
		return sb.toString();
	}

	//getメソッドで通信する。
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException {
		// ファイルの扱いを設定する。JSON設定
		response.setContentType("application/json;charset=UTF-8");
		// 出力の準備を行う
		PrintWriter out = response.getWriter();
		
		//返却値を格納するための変数を用意する。
		String retString = "";
		//受注伝票を取得するクエリを用意する
		String sql = "SELECT od.date AS ordered, od.order_code, og.organization as custom, od.est_delivery_date, p1.person_name AS inputter, ot.order_type, p2.person_name AS submitter, SUM(od.price * od. quantity) AS ammount FROM Order_ddt AS od, Person as p1, Person as p2, Organization as og, Order_Type as ot WHERE od.custom_organization_code = og.organization_code and od.deliver_organization_code = og.organization_code and od.inputter = p1.person_code and od.submitter = p2.person_code and od.order_type_code = ot.order_type_code GROUP BY order_code";
		//tryブロックを使い、エラー時の処理をする
		try {
			//JSONを取得する
			retString = this.getResult(sql);
		} catch (SQLException e) {
			// スタックトレースを出力する
			e.printStackTrace();
		}
			
		
		//作成したJSON文字列を出力し、クライアントへ渡す。
		out.print(retString);
		
		//開いた出力ストリームを閉じる。
		out.close();
	}
}
