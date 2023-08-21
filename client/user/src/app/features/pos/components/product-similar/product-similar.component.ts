import { Component, Input, OnInit } from '@angular/core';
import { ProductService } from 'src/app/features/vendor/services/product.service';
import { UserProductService } from '../../services/user-product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-similar',
  templateUrl: './product-similar.component.html',
  styleUrls: ['./product-similar.component.css'],
})
export class ProductSimilarComponent implements OnInit {
  @Input() categoryId!: any;
  similarProducts!: any;
  constructor(
    private productService: UserProductService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.productService
      .listProduct(
        undefined,
        4,
        undefined,
        undefined,
        undefined,
        this.categoryId?.toString(),
        undefined,undefined,undefined
      )
      .subscribe({
        next: (res) => {
          this.similarProducts = res.result;
          console.log(this.similarProducts, 'simillar');
        },
        error: (err) => {
          // console.log(err);
          //TODO manage the error handling
        },
      });
  }
  goToDetails(product: any) {
    let queryParams: {
      // productNameParams?: string;
      pid?: number;
    } = {};
    // queryParams.productNameParams = product.name;
    queryParams.pid = product.id;
    this.router.navigate(['/detail', product.name], { queryParams });
  }
}
