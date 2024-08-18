package ceui.pixiv.ui.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import ceui.lisa.R
import ceui.lisa.databinding.FragmentHomeViewpagerBinding
import ceui.loxia.ObjectType
import ceui.loxia.pushFragment
import ceui.pixiv.ui.common.PixivFragment
import ceui.pixiv.ui.common.ViewPagerFragment
import ceui.pixiv.session.SessionManager
import ceui.pixiv.ui.trending.TrendingTagsFragment
import ceui.pixiv.ui.trending.TrendingTagsFragmentArgs
import ceui.pixiv.ui.user.recommend.RecommendUsersFragment
import ceui.refactor.setOnClick
import ceui.refactor.viewBinding

class HomeViewPagerFragment : PixivFragment(R.layout.fragment_home_viewpager), ViewPagerFragment {
    private val binding by viewBinding(FragmentHomeViewpagerBinding::bind)
    private val viewModel by viewModels<HomeViewPagerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolbarLayout.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
            }
            binding.bottomInset.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                height = insets.bottom
            }
            windowInsets
        }

        binding.userIcon.setOnClick {
            pushFragment(R.id.navigation_mine_profile)
        }
        binding.naviSearch.setOnClick {
            pushFragment(R.id.navigation_search_viewpager)
        }

        binding.account = SessionManager.loggedInAccount
        binding.viewModel = viewModel
        binding.iconDiscoverTab.setOnClick {
            binding.homeViewPager.setCurrentItem(0, false)
        }
        binding.iconCirclesTab.setOnClick {
            binding.homeViewPager.setCurrentItem(1, false)
        }
        binding.iconChatsTab.setOnClick {
            binding.homeViewPager.setCurrentItem(2, false)
        }
        binding.iconFriendsTab.setOnClick {
            binding.homeViewPager.setCurrentItem(3, false)
        }
        binding.homeViewPager.isUserInputEnabled = false
        binding.homeViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.selectedTabIndex.value = position
            }
        })
        binding.homeViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 4
            }

            override fun createFragment(position: Int): Fragment {
                if (position == 0) {
                    return HomeFragment()
                } else if (position == 1) {
                    return TrendingTagsFragment().apply {
                        arguments = TrendingTagsFragmentArgs(ObjectType.ILLUST).toBundle()
                    }
                } else if (position == 2) {
                    return TrendingTagsFragment().apply {
                        arguments = TrendingTagsFragmentArgs(ObjectType.NOVEL).toBundle()
                    }
                } else {
                    return RecommendUsersFragment()
//                    return UserFollowingFragment().apply {
//                        arguments = UserFollowingFragmentArgs(SessionManager.loggedInUid, "public").toBundle()
//                    }
                }
            }
        }
    }
}