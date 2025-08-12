package com.aravindhan.zcartifact;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class homeController {

	private static void copy(InputStream is, OutputStream os) throws IOException {
		final byte[] buffer = new byte[8094];
		int n = 0;
		while (-1 != (n = is.read(buffer))) {
			os.write(buffer, 0, n);
		}
		os.flush();

	}
	public static String readFile(String filename) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(filename));
	    String nextLine = "";
	    StringBuffer sb = new StringBuffer();
	    while ((nextLine = br.readLine()) != null) {
	        sb.append(nextLine);
	    }
	    //remove newlines
	    String newString = sb.toString().replace('\n',' ');
	 
	    return newString;
	}
	String content_type = "";
	String imageName = "", reference = "",tag=null,digest = null,tagfile="",blobFile="";
// pull
	@RequestMapping(value = "/v2/**", method = { RequestMethod.GET, RequestMethod.HEAD })
	public void getManifest(HttpServletRequest request, HttpServletResponse response) throws IOException {


		String blobsFilePath = "/home/local/aravindh-pt4880/Documents/registry/blobs/";
		String repositoryFilePath = "/home/local/aravindh-pt4880/Documents/registry/repositories/";
		String fileName = "";

		String Path = request.getRequestURI();
		if (Path.equals("/v2/")) {

		} else {

		if (Path.contains("manifest")) {
			Path = request.getRequestURI();
			imageName = Path.substring(Path.indexOf("/v2/") + 4, Path.indexOf("manifests") - 1);
			reference = Path.substring(Path.indexOf("s/") + 2);
			if(reference.contains("sha256:")) 
				digest = reference;
			else
				tag = reference;
		
			
		if(!tag.equals(null)) {
		Path = request.getRequestURI();
		tagfile = repositoryFilePath+imageName+"/"+tag;
		digest =  readFile(tagfile);
		blobFile = blobsFilePath+digest;
		fileName = blobsFilePath+digest+"/content";
		content_type = blobsFilePath+digest+"/content_type";
		}
		}
		
		else if (Path.contains("blobs")){
			Path = request.getRequestURI();
			digest = Path.substring(Path.indexOf("s/") + 2);
			blobFile = blobsFilePath+digest;
			fileName = blobsFilePath+digest+"/content";
			content_type = blobsFilePath+digest+"/content_type";
		}
		

		request.getInputStream();
		response.setStatus(200);
		try (InputStream myStream = new FileInputStream(fileName)) {
			int available = myStream.available();
			response.setHeader("Content-Type",readFile(content_type));
			response.setHeader("Docker-Content-Digest", digest);
			if (request.getMethod().equals("GET")) {
				response.setContentLength(available);
				copy(myStream, response.getOutputStream());
				response.flushBuffer();
			} else {
				response.setHeader("Connection", "close");

			}
		}
		
		}
	}
	}
