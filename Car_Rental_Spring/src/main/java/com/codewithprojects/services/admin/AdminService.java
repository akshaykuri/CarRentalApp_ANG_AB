package com.codewithprojects.services.admin;

import java.io.IOException;
import java.util.List;
import com.codewithprojects.dto.BookACarDTO;
import com.codewithprojects.dto.CarDTO;
import com.codewithprojects.dto.CarDtoListDto;
import com.codewithprojects.dto.SearchCarDto;

public interface AdminService {
	boolean postCar(CarDTO carDto) throws IOException;

	List<CarDTO> getAllCars();

	void deleteCar(Long id);

	CarDTO getCarById(Long id);

	boolean updateCar(Long carId, CarDTO carDto) throws IOException;

	List<BookACarDTO> getBookings();

	boolean changeBookingStatus(Long bookingId, String status);

	CarDtoListDto searchCars(SearchCarDto searchCarDto);
}