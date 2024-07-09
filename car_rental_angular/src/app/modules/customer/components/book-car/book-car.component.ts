import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StorageService } from 'src/app/auth/services/storage/storage.service';
import { NzMessageService } from 'ng-zorro-antd/message';

@Component({
  selector: 'app-book-car',
  templateUrl: './book-car.component.html',
  styleUrls: ['./book-car.component.scss']
})
export class BookCarComponent {

  carId: number = this.activatedRoute.snapshot.params["id"];
  car: any;
  processedImg: any;
  vaildateForm!: FormGroup;
  isSpinning = false;
  dateFormat!: "DD-MM-YYYY";

  constructor(private service: CustomerService,
    private activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private message: NzMessageService,
    private router: Router) { }

  ngOnInit(){
    this.vaildateForm = this.fb.group({
      toDate:[null, Validators.required],
      fromDate:[null, Validators.required],
    })
    this.getCarById();
  }

  getCarById(){
    this.service.getCarById(this.carId).subscribe((res) => {
      // console.log(res);
      this.processedImg = 'data:image/jpeg;base64,' + res.returnedImage;
      this.car = res;
    })
  }

  bookACar(data: any){
    // console.log(data);
    this.isSpinning = true;
    let bookACarDto = {
      toDate: data.toDate,
      fromDate: data.fromDate,
      userId: StorageService.getUserId(),
      carId: this.carId
    }
    // console.log(bookACarDto);
    this.service.bookACar(this.carId,bookACarDto).subscribe((res) => {
      // console.log(res);
      this.isSpinning = false;
      this.message.success("Booking Request Submitted Successfully",{nzDuration: 5000});
      this.router.navigateByUrl("/customer/dashboard");
    },error => {
      this.message.error("Something went wrong", {nzDuration: 5000});
    })
  }
}
