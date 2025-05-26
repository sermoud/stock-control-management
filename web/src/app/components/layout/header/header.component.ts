import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  @Output() public sidenavToggle = new EventEmitter<void>();

  constructor() { }

  public onToggleSidenav = () => {
    this.sidenavToggle.emit();
  }
}
