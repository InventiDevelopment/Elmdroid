# Elmdroid

This library helps you implement [The Elm Architecture][tea] on Android. This well known unidirectional architecture
is incredibly easy to use but not so simple to set up - hence the reason to create this library. To make
things even simpler, we added integration with `LiveData` and optionally for `ViewModel`
from [android architecture components][arch] and `RxJava`.

Basic concepts
--------------
Basically only thing you have to implement is interface `Component<STATE : State, MSG : Msg, CMD : Cmd>`

![Runtime vs UI](readme-assets/runtime-vs-ui.png)

Component is than wrapped inside it's runtime. Runtime just receives messages from UI and translates them in to
the from of new states. UI then simply observes state `LiveData<State>` and renders the changes.

Usage
-----


Download
--------

```groovy
compile 'TODO'
```

License
-------

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