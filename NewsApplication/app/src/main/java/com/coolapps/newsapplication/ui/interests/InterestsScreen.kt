package com.coolapps.newsapplication.ui.interests

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import com.coolapps.newsapplication.R
import com.coolapps.newsapplication.data.Result
import com.coolapps.newsapplication.data.interests.InterestSection
import com.coolapps.newsapplication.data.interests.TopicSelection
import com.coolapps.newsapplication.data.interests.impl.FakeInterestsRepository
import com.coolapps.newsapplication.ui.theme.NewsApplicationTheme
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.runBlocking
import kotlin.math.max

enum class Sections(@StringRes val titleResId : Int){
    Topics(R.string.interests_section_topics),
    People(R.string.interests_section_people),
    Publication(R.string.interests_section_publications)
}

class TabContent(val section : Sections ,val content : @Composable () -> Unit)
@Composable
fun InterestsScreen(
    tabContent : List<TabContent>,
    currentSection: Sections,
    isExpandedScreen : Boolean,
    onTabChange : (Sections) -> Unit,
    openDrawer : () -> Unit,
    scaffoldState: ScaffoldState
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.cd_interests),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = if (!isExpandedScreen) {
                    {
                        IconButton(onClick = openDrawer) {
                            Icon(
                                painter = painterResource(R.drawable.ic_news_logo),
                                contentDescription = stringResource(R.string.cd_open_navigation_drawer),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                } else {
                    null
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(R.string.cd_search)
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp
            )
        }
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)
        InterestScreenContent(
            currentSection ,
            isExpandedScreen ,
            onTabChange ,
            tabContent,
            screenModifier
        )
    }
}
@Composable
fun rememberTabContent(interestsViewModel: InterestsViewModel): List<TabContent> {

    val uiState by interestsViewModel.uiState.collectAsState()
    val topicsSection = TabContent(Sections.Topics) {
        val selectedTopics by interestsViewModel.selectedTopics.collectAsState()
        TabWithSections(
            sections = uiState.topics,
            selectedTopics = selectedTopics,
            onTopicSelect = { interestsViewModel.toggleTopicSelection(it) }
        )
    }
    val peopleSection = TabContent(Sections.People) {
        val selectedPeople by interestsViewModel.selectedPeople.collectAsState()
        TabWithTopics(
            topics = uiState.people,
            selectedTopics = selectedPeople,
            onTopicSelect = { interestsViewModel.togglePersonSelected(it) }
        )
    }
    val publicationSection = TabContent(Sections.Publication) {
        val selectedPublications by interestsViewModel.selectedPublication.collectAsState()
        TabWithTopics(
            topics = uiState.publication,
            selectedTopics = selectedPublications,
            onTopicSelect = { interestsViewModel.togglePublicationSelected(it) }
        )
    }

    return listOf(topicsSection, peopleSection, publicationSection)
}
@Composable
fun InterestScreenContent(
    currentSection: Sections,
    isExpandedScreen: Boolean,
    updateSection: (Sections) -> Unit,
    tabContent: List<TabContent>,
    modifier: Modifier = Modifier

) {
    val selectedTabIndex = tabContent.indexOfFirst { it.section == currentSection }
    Column(modifier) {
        InterestsTabRow(selectedTabIndex, updateSection, tabContent, isExpandedScreen)
        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
        )
        Box(modifier = Modifier.weight(1f)) {
            tabContent[selectedTabIndex].content()
        }
    }
}
private val tabContainerModifier = Modifier
    .fillMaxWidth()
    .wrapContentWidth(Alignment.CenterHorizontally)
    .navigationBarsPadding(start = false, end = false)

@Composable
private fun TabWithTopics(
    topics: List<String>,
    selectedTopics: Set<String>,
    onTopicSelect: (String) -> Unit) {

    InterestsAdaptiveContentLayout(
        topPadding = 16.dp,
        modifier = tabContainerModifier.verticalScroll(rememberScrollState())
    ) {
        topics.forEach { topic ->
            TopicItem(
                itemTitle = topic,
                selected = selectedTopics.contains(topic),
                onToggle = { onTopicSelect(topic) },
            )
        }
    }
}
@Composable
private fun TabWithSections(
    sections: List<InterestSection>,
    selectedTopics: Set<TopicSelection>,
    onTopicSelect: (TopicSelection) -> Unit) {
    Column(tabContainerModifier.verticalScroll(rememberScrollState())) {
        sections.forEach { (section, topics) ->
            Text(
                text = section,
                modifier = Modifier
                    .padding(16.dp)
                    .semantics { heading() },
                style = MaterialTheme.typography.subtitle1
            )
            InterestsAdaptiveContentLayout {
                topics.forEach { topic ->
                    TopicItem(
                        itemTitle = topic,
                        selected = selectedTopics.contains(TopicSelection(section, topic)),
                        onToggle = { onTopicSelect(TopicSelection(section, topic)) },
                    )
                }
            }
        }
    }
}



@Composable
private fun TopicItem(
    itemTitle: String,
    selected: Boolean,
    onToggle: () -> Unit ,
    modifier: Modifier = Modifier
) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = modifier.toggleable(
                value = selected,
                onValueChange = { onToggle() }
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val image = painterResource(R.drawable.placeholder_1_1)
            Image(
                painter = image,
                contentDescription = null, // decorative
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Text(
                text = itemTitle,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f), // Break line if the title is too long
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(Modifier.weight(0.01f))
            SelectTopicButton(selected = selected)
        }
        Divider(
            modifier = modifier.padding(start = 72.dp, top = 8.dp, bottom = 8.dp),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
        )
    }
}


@Composable
private fun InterestsTabRow(
    selectedTabIndex: Int,
    updateSection: (Sections) -> Unit,
    tabContent: List<TabContent>,
    isExpandedScreen: Boolean
) {
    when (isExpandedScreen) {
        false -> {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                backgroundColor = MaterialTheme.colors.onPrimary,
                contentColor = MaterialTheme.colors.primary
            ) {
                InterestsTabRowContent(selectedTabIndex, updateSection, tabContent)
            }
        }
        true -> {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                backgroundColor = MaterialTheme.colors.onPrimary,
                contentColor = MaterialTheme.colors.primary,
                edgePadding = 0.dp
            ) {
                InterestsTabRowContent(
                    selectedTabIndex = selectedTabIndex,
                    updateSection = updateSection,
                    tabContent = tabContent,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun InterestsTabRowContent(
    selectedTabIndex: Int,
    updateSection: (Sections) -> Unit,
    tabContent: List<TabContent>,
    modifier: Modifier = Modifier
) {
    tabContent.forEachIndexed { index, content ->
        val colorText = if (selectedTabIndex == index) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
        }
        Tab(
            selected = selectedTabIndex == index,
            onClick = { updateSection(content.section) },
            modifier = Modifier.heightIn(min = 48.dp)
        ) {
            Text(
                text = stringResource(id = content.section.titleResId),
                color = colorText,
                style = MaterialTheme.typography.subtitle2,
                modifier = modifier.paddingFromBaseline(top = 20.dp)
            )
        }
    }
}
@Composable
private fun InterestsAdaptiveContentLayout(
    modifier: Modifier = Modifier,
    topPadding: Dp = 0.dp,
    itemSpacing: Dp = 4.dp,
    itemMaxWidth: Dp = 450.dp,
    multipleColumnsBreakPoint: Dp = 600.dp,
    content: @Composable () -> Unit,
) {
    Layout(modifier = modifier, content = content) { measurables, outerConstraints ->

        val multipleColumnsBreakPointPx = multipleColumnsBreakPoint.roundToPx()
        val topPaddingPx = topPadding.roundToPx()
        val itemSpacingPx = itemSpacing.roundToPx()
        val itemMaxWidthPx = itemMaxWidth.roundToPx()

        val columns = if (outerConstraints.maxWidth < multipleColumnsBreakPointPx) 1 else 2
        val itemWidth = if (columns == 1) {
            outerConstraints.maxWidth
        } else {
            val maxWidthWithSpaces = outerConstraints.maxWidth - (columns - 1) * itemSpacingPx
            (maxWidthWithSpaces / columns).coerceIn(0, itemMaxWidthPx)
        }
        val itemConstraints = outerConstraints.copy(maxWidth = itemWidth)
        val rowHeights = IntArray(measurables.size / columns + 1)
        val placeables = measurables.mapIndexed { index, measureable ->
            val placeable = measureable.measure(itemConstraints)
            val row = index.floorDiv(columns)
            rowHeights[row] = max(rowHeights[row], placeable.height)
            placeable
        }
        val layoutHeight = topPaddingPx + rowHeights.sum()
        val layoutWidth = itemWidth * columns + (itemSpacingPx * (columns - 1))
        layout(
            width = outerConstraints.constrainWidth(layoutWidth),
            height = outerConstraints.constrainHeight(layoutHeight)
        ) {
            var yPosition = topPaddingPx
            placeables.chunked(columns).forEachIndexed { rowIndex, row ->
                var xPosition = 0
                row.forEach { placeable ->
                    placeable.placeRelative(x = xPosition, y = yPosition)
                    xPosition += placeable.width + itemSpacingPx
                }
                yPosition += rowHeights[rowIndex]
            }
        }
    }
}

@Preview("Interests screen", "Interests")
@Composable
fun PreviewInterestsScreenDrawer(){
    NewsApplicationTheme {
        val tabContent = getFakeTabsContent()
        val (currentSection, updateSection) = rememberSaveable {
            mutableStateOf(tabContent.first().section)
        }
        InterestsScreen(
            tabContent = tabContent,
            currentSection = currentSection,
            isExpandedScreen = false,
            onTabChange = updateSection,
            openDrawer = { },
            scaffoldState = rememberScaffoldState()
        )
    }
}
@Preview(
    "Interests screen navrail (dark)", "Interests",
    uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_C
)
@Composable
fun PreviewInterestsScreenNavRail(){
    NewsApplicationTheme{
        val tabContent = getFakeTabsContent()
        val (currentSection, updateSection) = rememberSaveable {
            mutableStateOf(tabContent.first().section)
        }

        InterestsScreen(
            tabContent = tabContent,
            currentSection = currentSection,
            isExpandedScreen = true,
            onTabChange = updateSection,
            openDrawer = { },
            scaffoldState = rememberScaffoldState()
        )
    }
}
@Preview("Interests screen topics tab (dark)", "Topics", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTopicsTab(){
    val topics = runBlocking {
        (FakeInterestsRepository().getTopics() as Result.Success).data
    }
    NewsApplicationTheme {
        Surface {
            TabWithSections(topics, setOf() ) { }

        }

    }
}
@Preview("Interests screen people tab (dark)", "People", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPeopleTab() {
    val people = runBlocking {
        (FakeInterestsRepository().getPeople() as Result.Success).data
    }
    NewsApplicationTheme {
        Surface {
            TabWithTopics(people, setOf() ) {}

        }

    }
}

@Preview("Interests screen publications tab (dark)", "Publications", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPublicationsTab(){
    val publication = runBlocking {
        (FakeInterestsRepository().getPeople() as Result.Success).data
    }
    NewsApplicationTheme {
        Surface {
            TabWithTopics(publication, setOf() ) {}

        }

    }
}
fun getFakeTabsContent(): List<TabContent> {
    val interestsRepository = FakeInterestsRepository()
    val topicSelection = TabContent(Sections.Topics) {
        TabWithSections(
            runBlocking { (interestsRepository.getTopics() as Result.Success).data },
            emptySet()
        )
        { }
    }
    val peopleSelection = TabContent(Sections.People) {
        TabWithSections(
            runBlocking { (interestsRepository.getTopics() as Result.Success).data },
            emptySet()
        )
        { }
    }
    val publicationSelection = TabContent(Sections.Publication) {
        TabWithSections(
            runBlocking { (interestsRepository.getTopics() as Result.Success).data },
            emptySet()
        )
        { }
    }
    return listOf(topicSelection, peopleSelection, publicationSelection)
}




