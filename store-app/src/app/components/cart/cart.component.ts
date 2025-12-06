import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService, CartItem } from '../../services/cart.service';
import { Router } from '@angular/router'; 

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  cartItems: CartItem[] = [];
  total: number = 0;

  constructor(
    private cs: CartService, 
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cs.cart$.subscribe((items: CartItem[]) => {
      this.cartItems = items;
      this.total = this.cs.getTotal();
    });
  }

  removeItem(productId: string | undefined) {
    if (productId) {
      this.cs.removeFromCart(productId);
    }
  }

  checkout() {
    if (this.cartItems.length === 0) return;

    this.cs.checkout().subscribe({
      next: (res: any) => { 
        console.log('Compra realizada', res);
        alert('¡Compra realizada con éxito! Se ha descontado del stock.');
        this.cs.clearCart(); 
        this.router.navigate(['/']);
      },
      error: (err: any) => {
        console.error('Error en la compra', err);
        alert('Hubo un error al procesar la compra. Verifica el stock o tu sesión.');
      }
    });
  }
}