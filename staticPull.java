package com.zoho.zcartifact;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    //			HARD-CODED-EXAMPLE			
			if (Path.equals("/v2/ubuntu/manifests/20.04")) {
				fileName = "/home/local/aravindh-pt4880/Documents/data";
				digest = "sha256:9c152418e380c6e6dd7e19567bb6762b67e22b1d0612e4f5074bda6e6040c64a";
  			content_type = "application/vnd.docker.distribution.manifest.v2+json";
			}
			else if (Path.equals(
					"/v2/ubuntu/blobs/sha256:2b4cba85892afc2ad8ce258a8e3d9daa4a1626ba380677cee93ef2338da442ab")) {
				System.out.println("Inside blob_1");
				System.out.println("Inside manifest");
				fileName = "/home/local/aravindh-pt4880/Documents/layers/2b/data";
				digest = "sha256:2b4cba85892afc2ad8ce258a8e3d9daa4a1626ba380677cee93ef2338da442ab";
				content_type = "application/vnd.docker.container.image.v1+json";
			}

			else if (Path.equals(
					"/v2/ubuntu/blobs/sha256:7c3b88808835aa80f1ef7f03083c5ae781d0f44e644537cd72de4ce6c5e62e00")) {
				System.out.println("Inside blob_2");
				fileName = "/home/local/aravindh-pt4880/Documents/layers/7c/data";
				digest = "sha256:7c3b88808835aa80f1ef7f03083c5ae781d0f44e644537cd72de4ce6c5e62e00";
				content_type = "application/vnd.docker.image.rootfs.diff.tar.gzip";

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
