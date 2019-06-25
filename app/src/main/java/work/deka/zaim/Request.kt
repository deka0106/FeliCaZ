package work.deka.zaim

interface Request<T : Response> {
    fun execute(): T
}