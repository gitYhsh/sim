package com.xlcxx.utils;

import java.util.Vector;

/**  
 * 创建时间：2018年5月25日 下午12:56:18
 * 项目名称：taskmanager  
 * @author yhsh
 * @version 1.0
 * @since JDK 1.7.0_21 
 * 类说明：  
 */
public class HttpRespons {
	
	/**
	 * 连接信息
	 * **/
	private  Vector<String> contentCollection; 
	
	/**
	 * 连接url
	 * */
	private  String urlString ;
	/**
	 * default port number of the protocol associated
	 * **/
	private  int defaultPort;
	
	/**
	 * the file name of this <code>URL</code>.
	 * */
	private  String file;
	
	/**
	 * the host name of this <code>URL</code>
	 * **/
	private  String host;
	
	/**
	 * the path part of this <code>URL</code>.
	 * **/
	private  String path;
	
	
	/**
	 * the protocol name of this <code>URL</code>.
	 * */
	private  String protocol;
	
	
	/**
	 * he value of the <code>content-encoding</code> header field.
	 * **/
	private  String content;
	
	private  int code;
	
	private  String message;
	
	private  String method;
	
	
	
	

	public Vector<String> getContentCollection() {
		return contentCollection;
	}

	public void setContentCollection(Vector<String> contentCollection) {
		this.contentCollection = contentCollection;
	}

	public String getUrlString() {
		return urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public int getDefaultPort() {
		return defaultPort;
	}

	public void setDefaultPort(int defaultPort) {
		this.defaultPort = defaultPort;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}




	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	

	@Override
	public String toString() {
		return "HttpRespons [contentCollection=" + contentCollection
				+ ", urlString=" + urlString + ", defaultPort=" + defaultPort
				+ ", file=" + file + ", host=" + host + ", path=" + path
				+ ", protocol=" + protocol  
				+ ", content=" + content + ", contentEncoding="
				+  ", code=" + code + ", message=" + message
				+ ",  method=" + method + "]";
	}
	
	
}
