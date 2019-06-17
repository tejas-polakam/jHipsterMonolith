import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FirstmonolithSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [FirstmonolithSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [FirstmonolithSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FirstmonolithSharedModule {
  static forRoot() {
    return {
      ngModule: FirstmonolithSharedModule
    };
  }
}
