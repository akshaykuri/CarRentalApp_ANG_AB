import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NzMessageService } from 'ng-zorro-antd/message';

@Component({
  selector: 'app-update-car',
  templateUrl: './update-car.component.html',
  styleUrls: ['./update-car.component.scss']
})
export class UpdateCarComponent {

  carId:number=this.activatedRoute.snapshot.params["id"];
  isSpinning: boolean=false;
  imgChanged: boolean=false;
  selectedFile:any;
  imagePreview!: string | ArrayBuffer | null;
  existingImage: string | null = null;
  updateForm!: FormGroup;

  listOfOption: Array<{ label: string; value: string }> = [];
  listOfBrands = ["BMW","AUDI","FERRAI","TESLA","VOLVO","TOYOTA","HONDA","FORD","NISSAN","HYUNDAI","LEXUS","KIA"];
  listOfType = ["Petrol","Hybrid","Diesel","Electric","CNG"];
  listOfTransmission = ["Manual","Automatic"];
  listOfColor = ["Red","White","Blue","Black","Orange","Grey","Silver"];

  constructor(private adminService: AdminService,
  private activatedRoute: ActivatedRoute,
  private fb: FormBuilder,
  private message:NzMessageService,
  private router:Router){ }

  ngOnInit(){
    this.updateForm = this.fb.group({
      name: [null, Validators.required],
      brand: [null, Validators.required],
      type: [null, Validators.required],
      color: [null, Validators.required],
      transmission: [null, Validators.required],
      price: [null, Validators.required],
      description: [null, Validators.required],
      year: [null, Validators.required],
    })
    this.getCarById();
  }

  getCarById(){
    this.isSpinning = true;
    this.adminService.getCarById(this.carId).subscribe((res) => {
      // console.log(res);
      const carDto = res;
      this.existingImage = 'data:image/jpeg;base64,' + res.returnedImage;
      const yrDate = new Date(res.year);
      carDto.year = yrDate;
      // console.log(carDto);
      // console.log(this.existingImage);
      this.updateForm.patchValue(carDto);
      this.isSpinning = false;
    });
  }

  updateCar(){
    // console.log(this.updateForm.value);
    this.isSpinning = true;
    const formData: FormData = new FormData();
    if(this.imgChanged && this.selectedFile){
      formData.append("image",this.selectedFile);
    }
    formData.append("brand",this.updateForm.get('brand')!.value);
    formData.append("name",this.updateForm.get('name')!.value);
    formData.append("type",this.updateForm.get('type')!.value);
    formData.append("transmission",this.updateForm.get('transmission')!.value);
    formData.append("color",this.updateForm.get('color')!.value);
    formData.append("year",this.updateForm.get('year')!.value);
    formData.append("price",this.updateForm.get('price')!.value);
    formData.append("description",this.updateForm.get('description')!.value);
    // console.log(formData);
    this.adminService.updateCar(this.carId, formData).subscribe((res) =>{
      this.isSpinning = false;
      this.message.success("Car Details Updated Successfully.", { nzDuration: 5000 });
      this.router.navigateByUrl("/admin/dashboard");
      // console.log(res);
    }, error => {
      this.message.error("Error while Updating the Car Details.", { nzDuration: 5000 });
    })
  }

  onFileSelected(event: any){
    this.selectedFile = event.target.files[0];
    this.imgChanged = true;
    this.existingImage = null;
    this.previewImage();
  }

  previewImage(){
    const reader = new FileReader();
    reader.onload= () => {
      this.imagePreview = reader.result;
    }
    reader.readAsDataURL(this.selectedFile);
  }
}