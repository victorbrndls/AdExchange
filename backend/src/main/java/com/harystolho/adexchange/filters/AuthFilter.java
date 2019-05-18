package com.harystolho.adexchange.filters;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.harystolho.adexchange.auth.AuthService;

@Component
public class AuthFilter extends AbstractFilter {

	private AuthService tokenService;

	@Autowired
	public AuthFilter(AuthService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		if (req.getMethod().equals("OPTIONS")) {
			chain.doFilter(req, res);
			return;
		}

		Optional<String> token = getTokenFromHeader(req.getHeader("Authorization"));

		if (token.isPresent()) {
			Optional<String> accountId = tokenService.getAccountIdByToken(token.get());
			if (accountId.isPresent()) {
				req.setAttribute("ae.accountId", accountId.get());
				chain.doFilter(req, res);
				return;
			}
		}

		res.sendRedirect("/auth");
	}

	/**
	 * The token value is similar to "Token v1s1b7w29i4fbq5gi3za5h"
	 * 
	 * @param field
	 * @return
	 */
	private Optional<String> getTokenFromHeader(String field) {
		if (field == null)
			return Optional.empty();

		String[] fields = field.split(" ");

		String type = fields[0];
		String token = fields[1];

		if (type.equalsIgnoreCase("Token"))
			return Optional.of(token);

		return Optional.empty();
	}

}
