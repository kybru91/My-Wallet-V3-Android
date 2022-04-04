package piuk.blockchain.android.ui.interest.tbm.domain.usecase

import piuk.blockchain.android.ui.interest.tbm.domain.repository.AssetInterestRepository
import piuk.blockchain.android.ui.interest.tbm.domain.model.InterestDetail

class GetInterestDetailUseCase(private val repository: AssetInterestRepository) {
    suspend operator fun invoke(): Result<InterestDetail> = repository.getInterestDetail()
}