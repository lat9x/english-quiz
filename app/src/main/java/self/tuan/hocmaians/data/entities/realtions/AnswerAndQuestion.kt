package self.tuan.hocmaians.data.entities.realtions

import androidx.room.Embedded
import androidx.room.Relation
import self.tuan.hocmaians.data.entities.Question
import self.tuan.hocmaians.data.entities.UserAnswer

data class AnswerAndQuestion(
    @Embedded val userAnswer: UserAnswer,
    @Relation(
        parentColumn = "question_id",
        entityColumn = "id"
    )
    val question: Question
)