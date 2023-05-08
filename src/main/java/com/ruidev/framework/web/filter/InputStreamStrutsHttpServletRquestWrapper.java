package com.ruidev.framework.web.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class InputStreamStrutsHttpServletRquestWrapper extends HttpServletRequestWrapper {

	private byte[] body;

	public InputStreamStrutsHttpServletRquestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		ServletInputStream in = request.getInputStream();
		while((n = in.read(buffer)) != -1){
			out.write(buffer, 0, n);
		}
		this.body = out.toByteArray();
	}
	
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	}

	public byte[] getBody() {
		return body;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
		return new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public void setReadListener(ReadListener listener) {
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public boolean isFinished() {
				return false;
			}
		};
	}
}
