import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";

// @ts-ignore
import { v4 as uuid } from 'uuid';

@Component({
  selector: 'app-join',
  templateUrl: './join.component.html'
})
export class JoinComponent implements OnInit {
  joinForm: FormGroup;

  constructor(private fb: FormBuilder,
              private router: Router) {
    this.joinForm = this.fb.group({
      talkbotId: ['', Validators.required],
    })
  }

  ngOnInit(): void {
  }


  join() {
    const userId: string = uuid();
    if (this.joinForm.invalid) {
      return;
    }
    this.router.navigate([`/${this.joinForm.get('talkbotId')?.value}/chat/${userId}`]);
  }

}
