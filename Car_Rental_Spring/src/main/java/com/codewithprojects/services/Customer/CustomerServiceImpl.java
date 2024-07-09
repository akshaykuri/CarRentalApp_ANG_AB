package com.codewithprojects.services.Customer;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
import com.codewithprojects.entity.User;
import com.codewithprojects.enums.BookCarStatus;
import com.codewithprojects.repository.BookACarRepository;
import com.codewithprojects.repository.CarRepository;
import com.codewithprojects.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
	@Autowired
	private CarRepository carRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookACarRepository bookACarRepository;

	@Override
	public List<CarDTO> getAllCars(){
		return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
	}

	@Override
	public boolean bookACar(Long carId, BookACarDTO bookACarDto){
		Optional<Car> optionalCar = carRepository.findById(carId);
		Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());
		if(optionalCar.isPresent() && optionalUser.isPresent()){
			Car existingCar = optionalCar.get();
			BookACar bookACar = new BookACar();
			bookACar.setUser(optionalUser.get());
			bookACar.setCar(existingCar);
			bookACar.setFromDate(bookACarDto.getFromDate());
			bookACar.setToDate(bookACarDto.getToDate());
			bookACar.setBookCarStatus(BookCarStatus.PENDING);
			long diffInMilliSeconds = bookACarDto.getToDate().getTime() - bookACarDto.getFromDate().getTime();
			long days = TimeUnit.MILLISECONDS.toDays(diffInMilliSeconds);
			bookACar.setDays(days);
			bookACar.setPrice(existingCar.getPrice() * days);

			bookACarRepository.save(bookACar);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public CarDTO getCarById(Long cardId){
		Optional<Car> optionalCar = carRepository.findById(cardId);
		return optionalCar.map(Car::getCarDto).orElse(null);
	}

	@Override
	public List<BookACarDTO> getBookingsByUserId(Long userId) {
		return bookACarRepository.findAllByUserId(userId).stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
	}

	@Override
	public CarDtoListDto searchCars(SearchCarDto searchCarDto) {
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