//MySQLへの接続を行うクラス。

//試験用パッケージ。
package test;

//SQLパッケージからDB接続に必要なクラスをインポートする。
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


//DBマネージャー
public class DBManager {
    private static String driverName = "com.mysql.jdbc.Driver";			//ドライバ名。
    private static String url = "jdbc:mysql://localhost:3306/borg";		//MySQLのURL。
    private static String user = "user";								//MySQLのユーザ名。
    private static String pass = "user";								//MySQLのパスワード。

    /*
     * 関数名:public static ResultSet getResultSet(String sql)
     * 概要  :受け取ったSQLを実行し、結果セットを返す。
     * 引数  :String sql:DBに対し発行するクエリ。
     * 返却値  :ResultSet:DBから返された結果セット。
     * 作成者:T.Masuda
     * 作成日:2015.03.31
     */
    public static ResultSet getResultSet(String sql) {
    	//DBManagerクラスを利用し、DBへの接続を開始する。
    	Connection con = DBManager.getConnection();
    	//ステートメントを作成する。
    	Statement stmt = null;
    	ResultSet rs = null;
    	
    	//SQLエラーに対するtryブロック。
    	try {
    		stmt = con.createStatement();
    		//クエリを実行し、結果セットを取得する。
    		rs = stmt.executeQuery(sql);
    		//SQLエラーをキャッチしたら
    	} catch (SQLException e) {
    		// TODO 自動生成された catch ブロック
    		e.printStackTrace();
    		//必ず行う処理
    	} finally{
    		//SQLエラーに備えてtryブロックを配置する。
  //  		try {
///    			con.close();	//接続を閉じる
  //  			stmt.close();	//ステートメントを閉じる
    			//SQLエラーをキャッチしたら
  //  		} catch (SQLException e) {
   // 			e.printStackTrace();	//エラー時には内容を書き出す。
   // 		}
    	}
    	
    	//結果セットを返す。
    	return rs;
    }
    
    /*
     * 関数名: public static Statement getStatement()
     * 概要  :DB接続のステートメントを返す。
     * 引数  :なし
     * 返却値  :Statement:ステートメント。
     * 作成者:T.Masuda
     * 作成日:2015.04.06
     */
    public static Statement getStatement() {
    	//DBManagerクラスを利用し、DBへの接続を開始する。
    	Connection con = DBManager.getConnection();
    	//ステートメントを受け取る変数を用意する。
    	Statement stmt = null;
    	
    	//SQLエラーに対するtryブロック。
    	try {
    		//ステートメントを作成する
    		stmt = con.createStatement();
    		//SQLエラーをキャッチしたら
    	} catch (SQLException e) {
    		// TODO 自動生成された catch ブロック
    		e.printStackTrace();
    		//必ず行う処理
    	} 
    	
    	//ステートメントを返す。
    	return stmt;
    }
    
	/*
	 * 関数名:public static ResultSet getPreparedResultSet(String sql)
	 * 概要  :受け取ったSQLをプリペアードステートメントで実行し、結果セットを返す。
	 * 引数  :String sql:DBに対し発行するクエリ。
	 * 返却値  :ResultSet:DBから返された結果セット。
	 * 作成者:T.Masuda
	 * 作成日:2015.04.06
	 */
    public static ResultSet getPreparedResultSet(String sql) {
		//DBManagerクラスを利用し、DBへの接続を開始する。
		Connection con = DBManager.getConnection();
		//プリペアードステートメントを作成する。
		PreparedStatement pst = null;
		ResultSet rs = null;	//結果セットを用意する。
		
		//SQLエラーに対するtryブロック。
		try {
			pst = con.prepareStatement(sql);
			//クエリを実行し、結果セットを取得する。
			rs = pst.executeQuery();
		//SQLエラーをキャッチしたら
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		//必ず行う処理
		} finally{
			//SQLエラーに備えてtryブロックを配置する。
			try {
				con.close();	//接続を閉じる
				pst.close();	//ステートメントを閉じる
			//SQLエラーをキャッチしたら
			} catch (SQLException e) {
				e.printStackTrace();	//エラー時には内容を書き出す。
			}
		}
		
		//結果セットを返す。
		return rs;
	}
    
	/*
	 * 関数名:public static int executeUpdateQuery(String sql)
	 * 概要  :MyGalleryの写真を保存する。
	 * 引数  :保存するデータを含んだJSON。
	 * 戻り値:int:処理件数を返す。
	 * 作成日:2015.04.03
	 * 作成者:T.Masuda
	 */
    public static int executeUpdateQuery(String sql) throws SQLException{

		//DBManagerクラスを利用し、DBへの接続を開始する。
		Connection con = DBManager.getConnection();
		//ステートメントを作成する。
		Statement stmt = null;
		int retNum = 0;	//処理件数の変数を宣言、初期化する。
		
		//SQLエラーに対するtryブロック。
		try {
			stmt = con.createStatement();
			//クエリを実行し、結果セットを取得する。
			retNum = stmt.executeUpdate(sql);
		//SQLエラーをキャッチしたら
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		//必ず行う処理
		} finally{
			//SQLエラーに備えてtryブロックを配置する。
			try {
				con.close();	//接続を閉じる
				stmt.close();	//ステートメントを閉じる
			//SQLエラーをキャッチしたら
			} catch (SQLException e) {
				e.printStackTrace();	//エラー時には内容を書き出す。
			}
		}
		
		//処理件数を返す。
		return retNum;
	}
    
	/*
	 * 関数名:public static Connection getConnection()
	 * 概要  :DBとの接続を行い、その接続を返す。
	 * 引数  :なし。
	 * 返却値  :Connection:DBとの接続。
	 * 作成者:T.Masuda
	 * 作成日:2015.03.30
	 */
    public static Connection getConnection() {
        Connection con = null;			//DBとの接続のインスタンスを受け取る変数を宣言する。
        //エラーに備え、tryブロックを利用する。
        try {
        	//ドライバ名を取得する。
            Class.forName(driverName);
            //DBのURL、ユーザ名、パスワードをセットし、DBに接続する。
            con = DriverManager.getConnection(url,user,pass);
        //クラスが見つからなかったら
        } catch (ClassNotFoundException e) {
            e.printStackTrace();	//エラーログを出す。
        //SQL関連のエラーが発生したら
        } catch (SQLException e) {
            e.printStackTrace();	//エラーログを出す。
        }
        return con;
    }
}