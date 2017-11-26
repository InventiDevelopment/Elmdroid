package cz.inventi.elmdroid

import android.arch.lifecycle.ViewModel

/**
 * Created by Tomas Valenta on 24.11.2017.
 */
open class ElmComponentViewModel <STATE : State, in MSG : Msg, CMD : Cmd> (component: Component<STATE, MSG, CMD>) :
        ViewModel(),
        ComponentController<STATE, MSG> by ElmController<STATE, MSG, CMD>(component) {

    override fun onCleared() {
        super.onCleared()
        clear()
    }
}