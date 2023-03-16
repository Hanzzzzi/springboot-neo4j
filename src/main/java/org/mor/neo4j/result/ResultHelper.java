package org.omaha.neo4j.result;

public class ResultHelper {

	public static <T> ResultVO<T> getMsgSucResult(ResultCode resultCode, T t) {
		ResultVO<T> resultVO = new ResultVO<T>();
		resultVO.setOk(true);
		resultVO.setCode(resultCode.getCode());
		resultVO.setMessage(resultCode.getMessage());
		resultVO.setResult(t);
		return resultVO;
	}


	public static <T> ResultVO<T> getMsgFail(ResultCode resultCode, T t) {
		ResultVO<T> resultVO = new ResultVO<T>();
		resultVO.setCode(resultCode.getCode());
		resultVO.setOk(false);
		resultVO.setMessage(resultCode.getMessage());
		resultVO.setResult(t);
		return resultVO;
	}
	
	public static <T> ResultVO<T> getMsgFail(ResultCode resultCode) {
		ResultVO<T> resultVO = new ResultVO<T>();
		resultVO.setCode(resultCode.getCode());
		resultVO.setOk(false);
		resultVO.setMessage(resultCode.getMessage());
		return resultVO;
	}
	
	public static <T> ResultVO<T> getMsgSuc(ResultCode resultCode) {
		ResultVO<T> resultVO = new ResultVO<T>();
		resultVO.setCode(resultCode.getCode());
		resultVO.setOk(true);
		resultVO.setMessage(resultCode.getMessage());
		return resultVO;
	}

}
