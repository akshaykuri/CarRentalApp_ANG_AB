import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-my-bookings',
  templateUrl: './my-bookings.component.html',
  styleUrls: ['./my-bookings.component.scss']
})
export class MyBookingsComponent {

  isSpinning = false;
  bookings: any;

  constructor(private service: CustomerService){
    this.getMyBookings();
  }

  getMyBookings(){
    this.isSpinning = true;
    this.service.getBookingsByUserId().subscribe((res) => {
      // console.log(res);
      this.isSpinning = false;
      this.bookings = res;
    });
  }
}
