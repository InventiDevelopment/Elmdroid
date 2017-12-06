# Elmdroid

This library helps you implement [The Elm Architecture][tea] on Android. This well known unidirectional architecture
is incredibly easy to use but not so simple to set up - hence the reason to create this library. To make
things even simpler, we added integration with `LiveData` and optionally for `ViewModel`
from [android architecture components][arch] and `RxJava`.

## Basic concepts
Basically only thing you have to implement is interface `Component<STATE : State, MSG : Msg, CMD : Cmd>` which
is then wrapped inside it's runtime:

![Runtime vs UI](readme-assets/runtime-vs-ui.png)

Runtime just receives messages from UI and translates them in to the from of new states,
using `Component.update(..)` function.
UI then simply observes state `LiveData<State>` and renders the changes.

This is where your component fits in to the library, now let's have a look what is inside component
and how tu actually implement it.

## Usage

### Basic synchronous example

Let's say we want to implement simple screen with two buttons for increment and decrement and plain `TextView`
to keep track of the "score". You can find this example in [official elm examples][elm-simple-example]

First we have to define state, a data class holding information about the state of the screen. In this case
the only thing that can change is the current counter "score":

```kotlin
data class CounterState(val counter: Int) : State
```

Now let's define messages(Msg). You can thing about it as defining all the possible events that can
happen on a screen:

```kotlin
sealed class CounterMsg : Msg
object Increment : CounterMsg()
object Decrement : CounterMsg()
```

Now when we defined all the basic peaces, we can put everything together in `CounterComponent`

```kotlin
class CounterComponent: Component<CounterState, CounterMsg, CounterCmd> {
    override fun initState(): CounterState = CounterState(0)

    override fun update(msg: CounterMsg, prevState: CounterState): Pair<CounterState, CounterCmd?> = when(msg){
        is Increment -> CounterState(prevState.counter + 1).noCmd()
        is Decrement -> CounterState(prevState.counter - 1).noCmd()
    }
}
```

Ignore all the `Cmd` parts for now and have a look at the `update(msg, prevState)` function. It takes incoming
message with previous state and defines a new state to render. `initState()` simply defines what should be
the original state before any `Msg` arrives.

With prepared component, we can simply use it in our activity:

```kotlin
class CounterActivity : AppCompatActivity() {

    private lateinit var runtime: ElmRuntime<CounterState, CounterMsg, CounterCmd>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        supportActionBar?.title = getString(R.string.basic_counter)

        runtime = ElmRuntime(CounterComponent())

        runtime.state().observe(this, Observer {
            it?.let { counter.text = "${it.counter}" }
        })

        increment.setOnClickListener { runtime.dispatch(Increment) }
        decrement.setOnClickListener { runtime.dispatch(Decrement) }
    }

    override fun onDestroy() {
        super.onDestroy()
        runtime.clear()
    }
}
```

We need to wrap our component in `ElmRuntime` which gives use ability to observe current state as `LiveData`
and to dispatch messages, in this case `Increment` and `Decrement`. Also make sure you  call `clear()`
on runtime in `onDestroy()` to prevent memory leaks.

If you want your component to survive configuration change you have to handle it yourself or you can use
`ElmViewModel` and pass in your component

```groovy
 ElmComponentViewModel<LoginState, LoginMsg, LoginCmd>(LoginComponent())
```

or `ElmBaseViewModel` and implement your component logic right in it's subclass.
Eather way, your component will survive configuration change inside it's `ViewModel` a the `clear()`
will be called in `ViewModel.onCleared()` for you.

You can check the whole counter example in [samples][counter-sample]

### Commands and Subscriptions

TODO

## Download

```groovy
compile 'TODO'
```

## License

    Copyright 2017 INVENTI Development s.r.o.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.





[tea]: https://guide.elm-lang.org/architecture/
[arch]: https://developer.android.com/topic/libraries/architecture/index.html
[elm-simple-example]: http://elm-lang.org/examples/buttons
[counter-sample]: https://github.com/InventiDevelopment/Elmdroid/tree/dev/sample/src/main/java/com/example/elmdroid/counter