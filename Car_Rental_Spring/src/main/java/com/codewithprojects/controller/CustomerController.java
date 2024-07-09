package com.codewithprojects.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithprojects.dto.BookACarDTO;
import com.codewithprojects.dto.CarDTO;
import com.codewithprojects.dto.SearchCarDto;
import com.codewithprojects.services.Customer.CustomerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController{
	@Autowired
	private CustomerService customerService;

	@GetMapping("/cars")
	public ResponseEntity<List<CarDTO>> getAllCars(){
		List<CarDTO> carDtoList = customerService.getAllCars();
		return ResponseEntity.ok(carDtoList);
	}

	@PostMapping("/car/book/{carId}")
	public ResponseEntity<Void> bookACar(@PathVariable Long carId,@RequestBody BookACarDTO bookACarDto){
		boolean success = customerService.bookACar(carId, bookACarDto);
		if(success)
			return ResponseEntity.status(HttpStatus.CREATED).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@GetMapping("/car/{carId}")
	public ResponseEntity<CarDTO> getCarById(@PathVariable Long carId){
		CarDTO carDto = customerService.getCarById(carId);
		if(carDto == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(carDto);
	}

	@GetMapping("/car/bookings/{userId}")
	public ResponseEntity<List<BookACarDTO>> getBookingsByUserId(@PathVariable Long userId){
		return ResponseEntity.ok(customerService.getBookingsByUserId(userId));
	}

	@PostMapping("/car/search")
	public ResponseEntity<?> searchCar(@RequestBody SearchCarDto searchCarDto){
		return ResponseEntity.ok(customerService.searchCars(searchCarDto));
	}
}