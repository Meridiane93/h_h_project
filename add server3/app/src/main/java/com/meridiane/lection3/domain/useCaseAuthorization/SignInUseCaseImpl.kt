package com.meridiane.lection3.domain.useCaseAuthorization

import com.meridiane.lection3.domain.entity.Authorization
import com.meridiane.lection3.domain.repository.SignInRepository

interface SignInInterface {
    fun signIn(login:String,password:String): Authorization
}

class SignInUseCaseImpl(private val signInRepository: SignInRepository) :
    SignInInterface {

    override fun signIn(login:String,password:String): Authorization =
        signInRepository.getSignIn(login,password)

}