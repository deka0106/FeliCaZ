package work.deka.zaim

interface ZaimRequest<T : ZaimResponse> {
    fun execute(): T
}