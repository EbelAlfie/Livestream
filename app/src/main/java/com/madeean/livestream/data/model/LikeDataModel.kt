package com.madeean.livestream.data.model

import com.madeean.livestream.domain.entity.LikeDomainModel

data class LikeDataModel(
  val like:Int
){
  companion object{
    fun transform(likeDataModel:LikeDataModel):LikeDomainModel{
      return LikeDomainModel(likeDataModel.like)
    }
  }
}
