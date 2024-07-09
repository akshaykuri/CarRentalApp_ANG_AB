package com.codewithprojects.services.Customer;

import java.util.List;

import com.codewithprojects.dto.BookACarDTO;
import com.codewithprojects.dto.CarDTO;
import com.codewithprojects.dto.CarDtoListDto;
import com.codewithprojects.dto.SearchCarDto;

public interface CustomerService{
	List<CarDTO> getAllCars();
	boolean bookACar(Long carId, BookACarDTO bookACarDto);
	CarDTO getCarById(Long cardId);
	List<BookACarDTO> getBookingsByUserId(Long userId);
	CarDtoListDto searchCars(SearchCarDto searchCarDto);
}