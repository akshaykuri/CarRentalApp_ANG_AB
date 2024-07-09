package com.codewithprojects.services.admin;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.codewithprojects.dto.BookACarDTO;
import com.codewithprojects.dto.CarDTO;
import com.codewithprojects.dto.CarDtoListDto;
import com.codewithprojects.dto.SearchCarDto;
import com.codewithprojects.entity.BookACar;
import com.codewithprojects.entity.Car;
import com.codewithprojects.enums.BookCarStatus;
import com.codewithprojects.repository.BookACarRepository;
import com.codewithprojects.repository.CarRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	@Autowired
	private CarRepository carRepository;

	@Autowired
	private BookACarRepository bookACarRepository;

	@Override
	public boolean postCar(CarDTO carDto) throws IOException {
		try{
			Car car = new Car();
			car.setName(carDto.getName());
			car.setBrand(carDto.getBrand());
			car.setColor(carDto.getColor());
			car.setPrice(carDto.getPrice());
			car.setYear(carDto.getYear());
			car.setType(carDto.getType());
			car.setDescription(carDto.getDescription());
			car.setTransmission(carDto.getTransmission());
			car.setImage(carDto.getImage().getBytes());
			carRepository.save(car);
			return true;
		}catch(Exception e){
			System.out.println("error here-->"+e);
			return false;
		}
	}

	@Override
	public List<CarDTO> getAllCars(){
		return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
	}

	@Override
	public void deleteCar(Long id){
		carRepository.deleteById(id);
	}

	@Override
	public CarDTO getCarById(Long id){
		Optional<Car> optionalCar = carRepository.findById(id);
		return optionalCar.map(Car::getCarDto).orElse(null);
	}

	@Override
	public boolean updateCar(Long carId, CarDTO carDto) throws IOException{
		Optional<Car> optionalCar = carRepository.findById(carId);
		if(optionalCar.isPresent()){
			Car existingCar = optionalCar.get();
			if(carDto.getImage() != null){
				existingCar.setImage(carDto.getImage().getBytes());
			}
			existingCar.setBrand(carDto.getBrand());
			existingCar.setName(carDto.getName());
			existingCar.setType(carDto.getType());
			existingCar.setTransmission(carDto.getTransmission());
			existingCar.setColor(carDto.getColor());
			existingCar.setYear(carDto.getYear());
			existingCar.setPrice(carDto.getPrice());
			existingCar.setDescription(carDto.getDescription());
			carRepository.save(existingCar);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<BookACarDTO> getBookings(){
		return bookACarRepository.findAll().stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
	}

	@Override
	public boolean changeBookingStatus(Long bookingId, String status){
		Optional<BookACar> optionBookACar = bookACarRepository.findById(bookingId);
		if(optionBookACar.isPresent()){
			BookACar existignBookACar = optionBookACar.get();
			if(Objects.equals(status, "Approve"))
				existignBookACar.setBookCarStatus(BookCarStatus.APPROVED);
			else
				existignBookACar.setBookCarStatus(BookCarStatus.REJECTED);

			bookACarRepository.save(existignBookACar);
			return true;
		}
		return false;
	}

	@Override
	public CarDtoListDto searchCars(SearchCarDto searchCarDto){
		Car car = new Car();
		car.setBrand(searchCarDto.getBrand());
		car.setType(searchCarDto.getType());
		car.setTransmission(searchCarDto.getTransmission());
		car.setColor(searchCarDto.getColor());
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
				.withMatcher("brand", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("type", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("transmission", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("color", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Example<Car> carExample = Example.of(car, exampleMatcher);
		List<Car> carList = carRepository.findAll(carExample);
		CarDtoListDto carDtoListDto = new CarDtoListDto();
		carDtoListDto.setCarDtoList(carList.stream().map(Car::getCarDto).collect(Collectors.toList()));
		return carDtoListDto;
	}
}