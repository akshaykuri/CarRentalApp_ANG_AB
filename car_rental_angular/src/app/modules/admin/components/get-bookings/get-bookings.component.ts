import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { NzMessageService } from 'ng-zorro-antd/message';

@Component({
  selector: 'app-get-bookings',
  templateUrl: './get-bookings.component.html',
  styleUrls: ['./get-bookings.component.scss']
})
export class GetBookingsComponent {

  bookings: any;
  isSpinning = false;

  constructor(private service: AdminService,
    private mesage: NzMessageService){
    this.getBookings();
  }

  getBookings(){
    this.isSpinning = true;
    this.service.getCarBookings().subscribe((res) => {
      // console.log(res);
      this.isSpinning = false;
      this.bookings = res;
    });
  }

  changeBookingStatus(bookingId: number, status: string){
    this.isSpinning = true;
    // console.log(bookingId,status);
    this.service.changeBookingStatus(bookingId,status).subscribe((res) => {
      // console.log(res);
      this.isSpinning = false;
      this.getBookings();
      this.mesage.success("Booking Status Updated Successfully", {nzDuration: 5000});
    }, error => {
      this.mesage.error("Somethign went wrong", {nzDuration: 5000});
    });
  }
}
