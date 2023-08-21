import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ApiErrorHandlerService {
  constructor() {}
  private errorMessages: { [key: number]: string } = {
    1031: 'Current password is wrong',
    3005: 'New Password must differ',
    5051: 'Current and new password is same',
    1021: 'Invalid email format',
    1022: 'Email already exists',
    1023: 'User for this email not found',
    5000: 'Email or password is incorrect',
    5001: 'User blocked',
    1050: 'Token is invalid',
    1051: 'Token has expired',
    3000: 'Email max length should be 50',
    1071: 'Invalid Google token',
    3053: 'Category name already exists',
    1041: 'Invalid OTP format',
    1042: 'Incorrect OTP',
    1043: 'OTP expired',
    2030: 'Email is not verified',
    4073: 'Subcategory name already exists',
    4093: 'Subcategory ID already deleted',
    7667: 'Store name already exists in this area',
    2903: 'Pin code should be numbers',
    7668: 'Product name already exists for this store under the vendor',
    2816: 'Specification key value pair limit exceeded',
    2807: 'Stock should be between 1 and 999999',
    2810: 'Available stock should be between 1 and 999999',
    2812: 'Available stock must be less than or equal to total stock',
    2815: 'Price should be between 1 and 9999999',
    2844: 'Product image already added',
    2845: 'Image size exceeds our limit',
    9999: 'An error occurred while processing the email template.',
    2869: 'Product already exists in the cart',
    2866: 'Quantity out of range',
    2861: 'Invalid quantity',
    2863: 'Invalid start date',
    2865: 'Invalid end date',
    2070: 'Start date should be less than end date',
    2021: 'Product already exist on wishlist'
  };
  getErrorMessage(errorCode: number): string {
    return this.errorMessages[errorCode] || '007';
  }
}
