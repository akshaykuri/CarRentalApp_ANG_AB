import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-search-car',
  templateUrl: './search-car.component.html',
  styleUrls: ['./search-car.component.scss']
})
export class SearchCarComponent {
  searchCarForm!: FormGroup;
  cars: any = [];
  
  isSpinning = false;
  listOfOption: Array<{ label: string; value: string }> = [];
  listOfBrands = ["BMW","AUDI","FERRAI","TESLA","VOLVO","TOYOTA","HONDA","FORD","NISSAN","HYUNDAI","LEXUS","KIA"];
  listOfType = ["Petrol","Hybrid","Diesel","Electric","CNG"];
  listOfTransmission = ["Manual","Automatic"];
  listOfColor = ["Red","White","Blue","Black","Orange","Grey","Silver"];

  constructor(private fb: FormBuilder,
    private service: CustomerService){
    this.searchCarForm = this.fb.group({
      brand: [null],
      type: [null],
      transmission: [null],
      color: [null]
    });
  }

  searchCar(){
    this.isSpinning = true;
    this.service.searchCar(this.searchCarForm.value).subscribe((res) => {
      // console.log(res);
      this.cars = [];
      res.carDtoList.forEach((element: any) => {
        element.processedImg = 'data:image/jpeg;base64,'+ element.returnedImage;
        this.cars.push(element);
      });
      this.isSpinning = false;
    })
  }
}
