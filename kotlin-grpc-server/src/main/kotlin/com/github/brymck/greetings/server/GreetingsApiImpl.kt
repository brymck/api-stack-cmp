package com.github.brymck.greetings.server

import com.github.brymck.greetings.v1.GreetRequest
import com.github.brymck.greetings.v1.GreetResponse
import com.github.brymck.greetings.v1.GreetingsApiGrpc
import io.grpc.stub.StreamObserver

class GreetingsApiImpl : GreetingsApiGrpc.GreetingsApiImplBase() {
    override fun greet(req: GreetRequest, responseObserver: StreamObserver<GreetResponse>) {
        val reply = GreetResponse.newBuilder().setMessage("Hello ${req.name}").build()
        responseObserver.onNext(reply)
        responseObserver.onCompleted()
    }
}
