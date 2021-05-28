package fr.nicopico.kmmspacex

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}