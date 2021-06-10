package com.gd.qrserver;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App 
{
	static Logger log = LoggerFactory.getLogger(App.class); // log 출력
    public static void main( String[] args ) throws IOException
    {
      
       QRContent qrContent = new QRContent();
       qrContent.setPhone("000-0000-0000");
       qrContent.setPlace("구디아카데미");
       log.info("00. 기기 정보 : "+  qrContent.toString());
       
       Gson gson = new Gson();
       String param = gson.toJson(qrContent);
       log.info("00. 기기정보 json -> gson"+param.toString());
       
       URL url = new URL("http://localhost/addQRContent");
       HttpURLConnection conn = (HttpURLConnection)url.openConnection();
       conn.setConnectTimeout(5000); //5초
       conn.setReadTimeout(5000);
       conn.setRequestMethod("POST");
       log.info("00. 기기정보를 보낼 url의 방식 ="+url+"의 "+conn.getRequestMethod());
       log.info("00. 정보 연결/읽는데 지연시간 "+ conn.getConnectTimeout()/1000 +"초");
       
       //헤드내용 http 프로토콜 (소켓) -> 리퀘스트 : 1헤드(주소 get:파라미터) 2바디(post:파라미터) 
       conn.setRequestProperty("Content-Type","application/json");
       conn.setRequestProperty("Accept-Charset", "UTF-8");
       conn.setDoOutput(true);
       
       //바이트 단위의 outputStream을 글자단위의 outputStream 랩핑
       OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream()); //스트림이 아니라 래퍼. 매개변수없이 만들수없다.
       osw.write(param);
       osw.flush();
       
       int code = conn.getResponseCode(); //200 300 400 500 번대 응답코드
       //200 -> 정상입니다
       //300 -> 리다이레긑
       //400 -> 수정중입니다. 등등
       log.info("  conn.getResponseCode() "+  code);
       if ( code == 500) {
    	   log.info("1번 에러코드 Could not connect to address=(host=localhost)(port=3306)(type=master) : (conn=481) Unknown database = "+" sts spring (application.properties)에서 spring.datasource.url=jdbc:mariadb://localhost:3306/qr 디비명 확인해보셨나요");
    	   log.info("2번 에러코드You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near 'update )"+"update는 칼럽명으로 적절하지 않습니다.");
    	   log.info("3번 에러코드 Incorrect string value: '\\xEA\\xB5\\xAC\\xEB\\x94\\x94...' for column `qr`.`qrcontent`"+"heidi에서 "+"ALTER TABLE qrcontent CONVERT TO character SET utf8;"+"실행 해보셨나요?");
       } else if( code == 200) {
    	   log.info("정상입니다");
       }
    }
}
