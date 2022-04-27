package self.tuan.hocmaians.ui.fragments.quiz.result

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import self.tuan.hocmaians.R
import self.tuan.hocmaians.data.entities.Score
import self.tuan.hocmaians.databinding.FragmentReviewAnswersBinding
import self.tuan.hocmaians.util.CommonMethods
import self.tuan.hocmaians.util.Constants.GRAPH_FRAGMENT
import self.tuan.hocmaians.util.Constants.MAX_SCORE
import self.tuan.hocmaians.util.Status

/**
 * Get current recycler view item position
 *
 * stackoverflow.com/questions/38247602/android-how-can-i-get-current-positon-on-recyclerview-that-user-scrolled-to-item
 */
@AndroidEntryPoint
class ReviewAnswersFragment : Fragment(R.layout.fragment_review_answers) {

    // view binding
    private var _binding: FragmentReviewAnswersBinding? = null
    private val binding get() = _binding!!

    // view model
    private val viewModel: ReviewAnswersViewModel by viewModels()

    // hold all passed arguments
    private val args: ReviewAnswersFragmentArgs by navArgs()

    // review adapter
    private lateinit var reviewAnswersAdapter: ReviewAnswerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReviewAnswersBinding.bind(view)

        subscribeToObserver()

        loadReviewAdapter()

        viewModel.getQuestionAndAnswerList(action = args.whichFragment, timestamp = args.doneTime)

        viewModel.userAnswers.observe(viewLifecycleOwner) { answerAndQuestionList ->
            reviewAnswersAdapter.differ.submitList(answerAndQuestionList)
            viewModel.questionAnswerList = answerAndQuestionList
        }

        if (args.whichFragment == GRAPH_FRAGMENT) {
            binding.btnToGraph.visibility = View.GONE
        }

        // get user score and set up some text
        viewModel.getUserScore(args.doneTime).observe(viewLifecycleOwner) { score ->
            setupSomeText(score)
        }

        // help user to read review
        binding.ivReviewHelp.setOnClickListener {
            // show help dialog
            CommonMethods.showHelpDialog(
                context = requireContext(),
                title = getString(R.string.txt_review_answers_help_title),
                message = getString(R.string.txt_review_answers_help_message)
            )
        }

        // to graph
        binding.btnToGraph.setOnClickListener {
            val action = ReviewAnswersFragmentDirections
                .actionReviewAnswersFragmentToGraphFragment(args.topicId)
            findNavController().navigate(action)
        }
    }

    /**
     * Observe update question bookmark status
     */
    private fun subscribeToObserver() {
        viewModel.updateBookmark.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                binding.pgUpdateBookmark.visibility = View.GONE

                when (result.status) {
                    Status.SUCCESS -> {

//                        Snackbar.make(
//                            requireView(),
//                            "Update Bookmark Successfully",
//                            Snackbar.LENGTH_SHORT
//                        ).setAnchorView(binding.btnToGraph).show()
                        Toast.makeText(requireContext(), result.data, Toast.LENGTH_LONG).show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            requireContext(),
                            result.message ?: getString(R.string.unknown_error_occurred),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                        /* NO-OP */
                    }
                }
            }
        }
    }

    /**
     * Set up adapter for reviewing answer recyclerView.
     */
    private fun loadReviewAdapter() {
        reviewAnswersAdapter = ReviewAnswerAdapter(args.whichFragment)

        binding.apply {
            rvReviewAnswers.adapter = reviewAnswersAdapter
            rvReviewAnswers.layoutManager = LinearLayoutManager(requireContext())
            rvReviewAnswers.setHasFixedSize(true)
        }

        reviewAnswersAdapter.setOnBookmarkClickListener { isBookmarked, questionPos ->
            binding.pgUpdateBookmark.visibility = View.VISIBLE
            viewModel.onUpdateBookmark(isBookmarked = isBookmarked, questionsPosition = questionPos)
        }
    }

    /**
     * Set up 4 text view, including: total questions, total correct, overall score and done date
     *
     * @param score user score object
     */
    private fun setupSomeText(score: Score) {
        binding.apply {
            val totalQuestions =
                "${getString(R.string.total_questions)} ${score.totalQuestions}"
            val totalCorrect =
                "${getString(R.string.total_correct_answers)} ${score.totalCorrect}"

            val totalScore = "${getString(R.string.score)} ${
                CommonMethods.userScoreInString(
                    score.totalCorrect,
                    score.totalQuestions
                )
            } / $MAX_SCORE"

            val doneDate =
                "${getString(R.string.done_date)} ${CommonMethods.millisToDateTime(score.timestamp)}"

            tvTotalQuestions.text = totalQuestions
            tvTotalCorrectAnswers.text = totalCorrect
            tvDoneDate.text = doneDate
            tvScore.text = totalScore
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}