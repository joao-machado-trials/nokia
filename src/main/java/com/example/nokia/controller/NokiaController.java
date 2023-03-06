package com.example.nokia.controller;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nokia.service.NokiaService;

@RestController
@RequestMapping("/nokia")
public class NokiaController {

	@Autowired
	private NokiaService nokiaService;

	public NokiaController() { }

	public NokiaController(NokiaService nokiaService) {
		super();
		this.nokiaService = nokiaService;
	}

	@GetMapping
	public void main() throws SQLException {

		Connection connection = nokiaService.connect();

		System.out.println("Connected to H2 in-memory database.");

        nokiaService.collectMainMenu(connection);
	}
}
