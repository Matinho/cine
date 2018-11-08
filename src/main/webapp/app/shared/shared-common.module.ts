import { NgModule } from '@angular/core';

import { CineSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [CineSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [CineSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class CineSharedCommonModule {}
