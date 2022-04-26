package com.codeathon.service.impl;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.codeathon.service.PassswordService;

@Service("passswordService")
public class PasswordServiceImpl implements PassswordService {

	private final static Logger LOG = LoggerFactory.getLogger(PasswordServiceImpl.class);

	@Value("${cabin4j.password.encoding.algorithm:SHA-1}")
	private String encodingAlgorithm;

	@Override
	public boolean validatePassword(String password, String encoded) {
		if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(encoded)) {
			String enterEncoded = encodePassword(password).trim();
			if (StringUtils.isNotBlank(enterEncoded) && enterEncoded.equals(encoded)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String encodePassword(String password) {
		if (StringUtils.isNotBlank(password)) {
			try {
				MessageDigest md = MessageDigest.getInstance(encodingAlgorithm);
				ByteArrayInputStream fis = new ByteArrayInputStream(password.trim().getBytes());
				byte[] dataBytes = new byte[1024];
				int nread = 0;
				while ((nread = fis.read(dataBytes)) != -1) {
					md.update(dataBytes, 0, nread);
				}
				;
				byte[] mdbytes = md.digest();
				// convert the byte to hex format method 1
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < mdbytes.length; i++) {
					sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
				}
				return sb.toString();
			} catch (Exception e) {
				LOG.error("Exception occured while encoding password.", e);
			}
		}
		return null;
	}
}