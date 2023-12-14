import java.util.concurrent.locks.ReentrantLock

var countPhilosopher = 0

class Philosopher(val id: String, val leftFork: ReentrantLock, val rightFork: ReentrantLock) : Runnable {
    override fun run() {
        eat()
    }

    private fun eat() {
        var think = true
        while (think) {
            when ((1..2).random()) {
                1 -> {
                    if (rightFork.tryLock()) {
                        println("Философ $id обедает левой вилкой.")
                        think = false
                    } else if (!rightFork.tryLock() && !leftFork.tryLock()) {
                        think = false
                        println("Философ $id размышляет.")
                    }
                }

                2 -> {
                    if (leftFork.tryLock()) {
                        println("Философ $id обедает правой вилкой.")
                        think = false
                    }else if (!rightFork.tryLock() && !leftFork.tryLock()) {
                        think = false
                        println("Философ $id размышляет.")
                    }

                }
            }
        }
    }
}

fun main() {
    println("Сколько философов за круглым столом: ")
    print("Введите целое число: ")
    countPhilosopher = enter().countPhilosopher

    val forks = List(countPhilosopher) { ReentrantLock() }

    val philosophers = List(countPhilosopher) { id ->
        Philosopher(
            "Философ ${id + 1}",
            forks[id],
            forks[(id + 1) % forks.size]
        )
    }
    philosophers.forEach { Thread(it).start() }
}
//}

class enter {
    var countPhilosopher = 0
    init {
        var valid = false
        do {
            try {
                countPhilosopher = readLine()?.toInt() ?: 0
                valid = true
            } catch (e: NumberFormatException) {
                println("Ошибка. Введите целое число.")
            }
        } while (!valid)
    }
}